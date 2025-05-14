using System.Text.Json;
using Confluent.Kafka;
using LAB2.Data;
using LAB2.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;

namespace LAB2.Kafka;

public class KafkaConsumerService : BackgroundService
{
    private readonly string _topic;
    private readonly IConsumer<Ignore, string> _consumer;
    private readonly IServiceProvider _serviceProvider;
    private readonly ILogger<KafkaConsumerService> _logger;

    public KafkaConsumerService(
        IOptions<KafkaSettings> settings,
        IServiceProvider serviceProvider,
        ILogger<KafkaConsumerService> logger)
    {
        var config = new ConsumerConfig
        {
            BootstrapServers = settings.Value.BootstrapServers,
            GroupId = "publisher-group",
            AutoOffsetReset = AutoOffsetReset.Earliest,
            EnableAutoCommit = false,
            AllowAutoCreateTopics = true
        };

        _consumer = new ConsumerBuilder<Ignore, string>(config).Build();
        _topic = settings.Value.OutTopic;
        _serviceProvider = serviceProvider;
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
                        _logger.LogInformation("Processing message {Id} with status {State}", message.Id, message.State);

                        using var scope = _serviceProvider.CreateScope();
                        var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();

                        var dbMessage = await dbContext.Messages
                            .FirstOrDefaultAsync(m => m.id == message.Id, stoppingToken);

                        if (dbMessage != null)
                        {
                            dbMessage.State = message.State;
                            await dbContext.SaveChangesAsync(stoppingToken);
                            _logger.LogInformation("Updated message {Id} status to {State}", message.Id, message.State);
                        }
                        else
                        {
                            _logger.LogWarning("Message {Id} not found in database", message.Id);
                        }
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