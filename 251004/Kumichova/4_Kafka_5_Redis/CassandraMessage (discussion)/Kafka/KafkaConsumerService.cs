using System.Text.Json;
using Confluent.Kafka;
using Microsoft.Extensions.Options;
using Microsoft.Extensions.Logging;

namespace CassandraMessages.Kafka;

public class KafkaConsumerService : BackgroundService
{
    private readonly KafkaProducerService _producerService;
    private readonly string _topic;
    private readonly IConsumer<Ignore, string> _consumer;
    private readonly ILogger<KafkaConsumerService> _logger;

    public KafkaConsumerService(
        KafkaProducerService producerService,
        IOptions<KafkaSettings> settings,
        ILogger<KafkaConsumerService> logger)
    {
        var config = new ConsumerConfig
        {
            BootstrapServers = settings.Value.BootstrapServers,
            GroupId = "discussion-group",
            AutoOffsetReset = AutoOffsetReset.Earliest,
            EnableAutoCommit = false,
            AllowAutoCreateTopics = true
        };

        _consumer = new ConsumerBuilder<Ignore, string>(config).Build();
        _topic = settings.Value.InTopic;
        _producerService = producerService;
        _logger = logger;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        await Task.Delay(5000, stoppingToken); // Даем время Kafka запуститься
        
        _logger.LogInformation("Subscribing to Kafka topic: {Topic}", _topic);
        _consumer.Subscribe(_topic);

        try
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    var consumeResult = _consumer.Consume(stoppingToken);

                    if (consumeResult != null)
                    {
                        _logger.LogInformation("Received message from Kafka: {Value}", consumeResult.Message.Value);
                        
                        var message = JsonSerializer.Deserialize<MessageKafkaDto>(consumeResult.Message.Value);
                        _logger.LogInformation("Moderating message {Id}", message.Id);

                        // Модерация: если содержит "badword" --- DECLINE, иначе APPROVE
                        if (message.Content.Contains("badword", StringComparison.OrdinalIgnoreCase))
                        {
                            message.State = "DECLINE";
                            _logger.LogWarning("Message {Id} declined due to bad word", message.Id);
                        }
                        else
                        {
                            message.State = "APPROVE";
                            _logger.LogInformation("Message {Id} approved", message.Id);
                        }

                        await _producerService.ProduceAsync(message);
                        _logger.LogInformation("Sent moderated message {Id}: {State}", message.Id, message.State);
                    }
                }
                catch (ConsumeException ex)
                {
                    _logger.LogError(ex, "Kafka consume error: {Reason}", ex.Error.Reason);
                    await Task.Delay(5000, stoppingToken); // Пауза перед повторной попыткой
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Error processing Kafka message");
                }
            }
        }
        catch (OperationCanceledException)
        {
            _logger.LogInformation("Kafka consuming canceled.");
        }
        finally
        {
            _consumer.Close();
            _consumer.Dispose();
        }
    }
}