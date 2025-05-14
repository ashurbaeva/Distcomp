using Cassandra.Mapping;
using CassandraMessages.Models;

namespace CassandraMessages.Repositories;

public interface IMessageRepository
{
    Task<Message> CreateAsync(Message message);
    Task<Message?> GetByIdAsync(Guid id);
    Task<IPage<Message>> GetAllAsync(int pageSize = 10, byte[]? pagingState = null);
    Task<Message> UpdateAsync(Message message);
    Task<bool> DeleteAsync(Guid id);
    Task<IEnumerable<Message>> GetByTopicIdAsync(int topicId);
}