using Cassandra;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using ISession = Cassandra.ISession;

namespace Discussion.Repositories.Implementations;

public class CassandraNoteRepository : INoteRepository
{
    private readonly ISession _session;
    
    public CassandraNoteRepository(ISession session)
    {
        _session = session;
    }
    
    private Note MapNoteFromRow(Row row)
    {
        return new Note
        {
            Id = row.GetValue<long>("id"),
            IssueId = row.GetValue<long>("issue_id"),
            Content = row.GetValue<string>("content"),
        };
    }
    
    public async Task<IEnumerable<Note>> GetAllAsync()
    {
        var query = "SELECT * FROM tbl_message";
        var result = await _session.ExecuteAsync(new SimpleStatement(query));
        return result.Select(MapNoteFromRow).ToList();
    }

    public async Task<Note?> GetByIdAsync(long id)
    {
        var query = "SELECT * FROM tbl_message WHERE id = ?";
        var result = await _session.ExecuteAsync(new SimpleStatement(query, id));
        var row = result.FirstOrDefault();
        return row != null ? MapNoteFromRow(row) : null;
    }

    public async Task<Note> CreateAsync(Note entity)
    {
        var query = "INSERT INTO tbl_message (id, issue_id, content) VALUES (?, ?, ?)";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(entity.Id, entity, entity.Content);
        var a = await _session.ExecuteAsync(boundStatement);
        foreach (var item in a.GetRows())
        {
            Console.WriteLine(item);
        }
        return entity;
    }

    public async Task<Note?> UpdateAsync(Note entity)
    {
        var existingNote = await GetByIdAsync(entity.Id);
        if (existingNote == null)
            return null;

        var query = "UPDATE tbl_message SET story_id = ?, content = ? WHERE id = ?";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(entity.IssueId, entity.Content, entity.Id);
        await _session.ExecuteAsync(boundStatement);
        return entity;
    }

    public async Task<bool> DeleteAsync(long id)
    {
        var existingNote = await GetByIdAsync(id);
        if (existingNote == null)
            return false;

        var query = $"DELETE FROM tbl_message WHERE id = ?";
        var statement = await _session.PrepareAsync(query);
        var boundStatement = statement.Bind(id);
        await _session.ExecuteAsync(boundStatement);
        return true;
    }
}