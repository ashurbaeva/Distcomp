using System.Text.Json;
using Confluent.Kafka;
using Microsoft.Extensions.Options;

namespace CassandraMessages.Kafka;

public class KafkaProducerService
{
    private readonly string _topic;
    private readonly IProducer<Null, string> _producer;

    public KafkaProducerService(IOptions<KafkaSettings> settings)
    {
        var config = new ProducerConfig
        {
            BootstrapServers = settings.Value.BootstrapServers,
            AllowAutoCreateTopics = true
        };
        _producer = new ProducerBuilder<Null, string>(config).Build();
        _topic = settings.Value.OutTopic;
    }

    public async Task ProduceAsync<T>(T message)
    {
        var json = JsonSerializer.Serialize(message);
        await _producer.ProduceAsync(_topic, new Message<Null, string> { Value = json });
    }
}