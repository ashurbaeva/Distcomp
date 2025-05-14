using Confluent.Kafka;
using System.Text.Json;
using Shared.Models.DTOs.Requests;

namespace Publisher.Kafka;

using Confluent.Kafka;
using System.Text.Json;
using System.Threading.Tasks;

public class PublisherProducer
{
    private readonly IProducer<string, string> _producer;
    private const string Topic = "InTopic"; // Kafka topic name

    public PublisherProducer(IProducer<string, string> producer)
    {
        _producer = producer;
    }

    public async Task SendReactionCreateAsync(ReactionRequestTo request)
    {
        var message = new Message<string, string>
        {
            Key = request.Id ?? Guid.NewGuid().ToString(),
            Value = JsonSerializer.Serialize(new
            {
                Action = "create",
                Payload = request
            })
        };

        await _producer.ProduceAsync(Topic, message);
    }

    public async Task SendReactionUpdateAsync(ReactionRequestTo request)
    {
        var message = new Message<string, string>
        {
            Key = request.Id ?? Guid.NewGuid().ToString(),
            Value = JsonSerializer.Serialize(new
            {
                Action = "update",
                Payload = request
            })
        };

        await _producer.ProduceAsync(Topic, message);
    }

    public async Task SendReactionDeleteAsync(string id)
    {
        var message = new Message<string, string>
        {
            Key = id,
            Value = JsonSerializer.Serialize(new
            {
                Action = "delete",
                Payload = new { Id = id }
            })
        };

        await _producer.ProduceAsync(Topic, message);
    }

    public async Task SendReactionGetByIdAsync(string id)
    {
        var message = new Message<string, string>
        {
            Key = id,
            Value = JsonSerializer.Serialize(new
            {
                Action = "getbyid",
                Payload = new { Id = id }
            })
        };

        await _producer.ProduceAsync(Topic, message);
    }

    public async Task SendReactionGetAllAsync(string? country = null)
    {
        var key = country ?? Guid.NewGuid().ToString();
        var message = new Message<string, string>
        {
            Key = key,
            Value = JsonSerializer.Serialize(new
            {
                Action = "getall",
                Payload = new { Country = country }
            })
        };

        await _producer.ProduceAsync(Topic, message);
    }
}
