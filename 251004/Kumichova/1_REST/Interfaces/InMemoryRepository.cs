namespace LAB1.Interfaces;

public class InMemoryRepository<T> : IRepository<T> where T : class
{
    private readonly List<T> _entities = new();
    private int _idCounter = 0;

    public Task<IEnumerable<T>> GetAllAsync() 
        => Task.FromResult(_entities.AsEnumerable());

    public Task<T> GetByIdAsync(int id)
        => Task.FromResult(_entities.FirstOrDefault(e => (e as dynamic).Id == id));

    public Task<T> CreateAsync(T entity)
    {
        (entity as dynamic).Id = ++_idCounter;
        _entities.Add(entity);
        return Task.FromResult(entity);
    }

    public Task<T> UpdateAsync(T entity)
    {
        var index = _entities.FindIndex(e => (e as dynamic).Id == (entity as dynamic).Id);
        if (index != -1) _entities[index] = entity;
        return Task.FromResult(entity);
    }

    public Task<bool> DeleteAsync(int id)
    {
        var entity = _entities.FirstOrDefault(e => (e as dynamic).Id == id);
        if (entity == null) return Task.FromResult(false);
        return Task.FromResult(_entities.Remove(entity));
    }
}