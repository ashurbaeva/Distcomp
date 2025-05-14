using Confluent.Kafka;
using Microsoft.Extensions.Hosting;
using Shared.Models.DTOs.Responses;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

namespace Publisher.Kafka;

public class PublisherConsumer
{
    private readonly IConsumer<string, string> _consumer;

    public PublisherConsumer(IConsumer<string, string> consumer)
    {
        _consumer = consumer;
    }

    public void Start(CancellationToken cancellationToken)
    {
        _consumer.Subscribe("OutTopic");

        while (!cancellationToken.IsCancellationRequested)
        {
            try
            {
                var consumeResult = _consumer.Consume(cancellationToken);
                var reactionResponse = JsonSerializer.Deserialize<ReactionResponseTo>(consumeResult.Message.Value);

                // Process the response, e.g., update status or log the result
                Console.WriteLine($"Processed reaction: {reactionResponse?.State}");
            }
            catch (ConsumeException e)
            {
                Console.WriteLine($"Error consuming message: {e.Error.Reason}");
            }
        }

        _consumer.Close();
    }
}
