using Cassandra;
using Cassandra.Mapping;
using CassandraMessages.Models;

namespace CassandraMessages.Repositories;

public class MessageRepository : IMessageRepository
{
    private readonly IMapper _mapper;

    public MessageRepository(ICluster cluster)
    {
        var session = cluster.Connect("distcomp");
        _mapper = new Mapper(session);

        // Инициализация таблицы
        session.Execute(@"
            CREATE TABLE IF NOT EXISTS tbl_message (
                id uuid PRIMARY KEY,
                content text,
                topic_id int,
                created_at timestamp,
                country text,
                state text
            )");
        
        // Создание индекса для topic_id для улучшения производительности запросов
        session.Execute("CREATE INDEX IF NOT EXISTS ON tbl_message(topic_id)");
    }

    public async Task<Message> CreateAsync(Message message)
    {
        message.Id = Guid.NewGuid();
        message.CreatedAt = DateTime.UtcNow;
        await _mapper.InsertAsync(message);
        return message;
    }

    public async Task<Message?> GetByIdAsync(Guid id)
    {
        return await _mapper.FirstOrDefaultAsync<Message>("WHERE id = ?", id);
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