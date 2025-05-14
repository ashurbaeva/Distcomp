namespace Messaging.MessageBus.Interfaces;

public interface INoteBus<TK, TV>
{
    Task PublishAsync(TK key, TV message);
}