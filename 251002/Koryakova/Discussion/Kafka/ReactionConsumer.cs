using Confluent.Kafka;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.DependencyInjection;
using System.Text.Json;
using Discussion.Services;
using Discussion.Storage;
using Discussion.Interfaces;
using Shared.Models;
using Shared.Models.DTOs.Requests;
using AutoMapper;
using Discussion.Models.Entities;

namespace Discussion.Kafka;

public class ReactionConsumer
{
    private readonly ICrudRepository<Reaction> _reactionRepository;
    private readonly IProducer<string, string> _producer;
    private readonly string _outTopic = "OutTopic";
    private readonly List<string> _stopWords = new() { "spam", "fake", "badword" };

    public ReactionConsumer(
        ICrudRepository<Reaction> reactionRepository,
        IProducer<string, string> producer)
    {
        _reactionRepository = reactionRepository;
        _producer = producer;
    }

    public async Task StartAsync(CancellationToken cancellationToken)
    {
        var config = new ConsumerConfig
        {
            BootstrapServers = "localhost:9092",
            GroupId = "discussion-service",
            AutoOffsetReset = AutoOffsetReset.Earliest
        };

        await Task.Run(async () =>
        {
            using var consumer = new ConsumerBuilder<Ignore, string>(config).Build();
            consumer.Subscribe("InTopic");

            while (!cancellationToken.IsCancellationRequested)
            {
                try
                {
                    var cr = consumer.Consume(cancellationToken);
                    var json = cr.Message.Value;

                    var reaction = JsonSerializer.Deserialize<Reaction>(json);
                    if (reaction != null)
                    {
                        // Moderate & update
                        reaction.State = ContainsStopWords(reaction.Content)
                            ? ReactionState.DECLINED
                            : ReactionState.APPROVED;

                        await _reactionRepository.UpdateAsync(reaction);

                        var outJson = JsonSerializer.Serialize(reaction);
                        await _producer.ProduceAsync("OutTopic", new Message<string, string>
                        {
                            Key = reaction.Id,
                            Value = outJson
                        });
                    }
                }
                catch (ConsumeException e)
                {
                    Console.WriteLine($"Consume error: {e.Error.Reason}");
                }
            }

            consumer.Close();
        }, cancellationToken);
    }


    private bool ContainsStopWords(string content)
    {
        return _stopWords.Any(word =>
            content.Contains(word, StringComparison.OrdinalIgnoreCase));
    }
}
