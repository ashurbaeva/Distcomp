using Cassandra.Mapping;
using CassandraMessages.Models;
using Cassandra;

namespace CassandraMessages.Services;

public class MessageService
{
    private readonly IMapper _mapper;

    public MessageService(ICluster cluster)
    {
        var session = cluster.Connect("distcomp");
        _mapper = new Mapper(session);
    }

    public async Task<Message?> GetByIdAsync(Guid id)
    {
        return await _mapper.FirstOrDefaultAsync<Message>("WHERE id = ?", id);
    }

    // Остальные методы остаются без изменений
    public async Task<Message> CreateAsync(Message message)
    {
        message.Id = Guid.NewGuid();
        message.CreatedAt = DateTime.UtcNow;
        await _mapper.InsertAsync(message);
        return message;
    }

    public async Task<Message?> GetFirstByTopicIdAsync(int topicId)
    {
        return await _mapper.FirstOrDefaultAsync<Message>("WHERE topic_id = ?", topicId);
    }

    
    public async Task<IPage<Message>> GetAllAsync(int pageSize = 10, byte[]? pagingState = null)
    {
        return await _mapper.FetchPageAsync<Message>(
            pageSize,
            pagingState,
            "SELECT * FROM tbl_message",
            Array.Empty<object>()
        );
    }

    public async Task<Message> UpdateAsync(Message message)
    {
        await _mapper.UpdateAsync(message);
        return message;
    }

    public async Task<bool> DeleteAsync(Guid id)
    {
        await _mapper.DeleteAsync<Message>("WHERE id = ?", id);
        return true;
    }

    public async Task<IEnumerable<Message>> GetByTopicIdAsync(int topicId)
    {
        return await _mapper.FetchAsync<Message>("WHERE topic_id = ?", topicId);
    }
}

/*using Cassandra;
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
    
    /*private readonly IMapper _mapper;

    public MessageService(ICluster cluster)
    {
        var session = cluster.Connect("distcomp");
        _mapper = new Mapper(session);
    }
    
    public async Task<Message?> GetByIdAsync(Guid id)
    {
        return await _mapper.FirstOrDefaultAsync<Message>("WHERE id = ?", id);
    }#1#

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
}*/