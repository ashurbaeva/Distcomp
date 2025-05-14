using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using System.Text.Json;
using Shared.Models.DTOs.Responses;
using Discussion.Models.Entities;

namespace Discussion.Kafka;

public class ReactionProducer
{
    private readonly IProducer<string, string> _producer;
    private readonly string _topic = "OutTopic";

    public ReactionProducer(IProducer<string, string> producer)
    {
        _producer = producer;
    }

    public async Task SendReactionAsync(Reaction reaction)
    {
        if (reaction == null) throw new ArgumentNullException(nameof(reaction));

        var message = new Message<string, string>
        {
            Key = reaction.Id,
            Value = JsonSerializer.Serialize(reaction)
        };

        await _producer.ProduceAsync(_topic, message);
    }
}
