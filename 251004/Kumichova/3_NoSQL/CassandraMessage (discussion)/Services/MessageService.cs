using Cassandra.Mapping;
using CassandraMessages.Models;
using CassandraMessages.Repositories;

namespace CassandraMessages.Services;

public class MessageService
{
    private readonly IMessageRepository _repository;

    public MessageService(IMessageRepository repository)
    {
        _repository = repository;
    }

    public async Task<Message> CreateAsync(Message message)
    {
        return await _repository.CreateAsync(message);
    }

    public async Task<Message?> GetByIdAsync(Guid id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<IPage<Message>> GetAllAsync(int pageSize = 10)
    {
        return await _repository.GetAllAsync(pageSize);
    }

    public async Task<Message> UpdateAsync(Message message)
    {
        return await _repository.UpdateAsync(message);
    }

    public async Task<bool> DeleteAsync(Guid id)
    {
        return await _repository.DeleteAsync(id);
    }

    public async Task<IEnumerable<Message>> GetByTopicIdAsync(int topicId)
    {
        return await _repository.GetByTopicIdAsync(topicId);
    }
}