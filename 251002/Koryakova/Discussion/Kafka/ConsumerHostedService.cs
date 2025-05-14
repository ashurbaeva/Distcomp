namespace Discussion.Kafka
{
    public class ConsumerHostedService : BackgroundService
    {
        private readonly IServiceProvider _serviceProvider;

        public ConsumerHostedService(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            using var scope = _serviceProvider.CreateScope();
            var consumer = scope.ServiceProvider.GetRequiredService<ReactionConsumer>();

            await consumer.StartAsync(stoppingToken);
        }
    }

}
