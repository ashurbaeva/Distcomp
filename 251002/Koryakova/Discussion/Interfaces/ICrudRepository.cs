using Shared.Models.Queries;

namespace Discussion.Interfaces
{
    public interface ICrudRepository<T> where T : class
    {
        Task<T?> GetByIdAsync(string id);
        Task<IEnumerable<T>> GetAllAsync();
        Task<T> AddAsync(T entity);
        Task<T> UpdateAsync(T entity);
        Task<bool> DeleteByIdAsync(string id);
        Task<IEnumerable<T>> GetFilteredAsync(QueryOptions<T> options);

        T? GetById(string id);
        IEnumerable<T> GetAll();
        T Add(T entity);
        T Update(T entity);
        bool DeleteById(string id);
        IEnumerable<T> GetFiltered(QueryOptions<T> options);
    }
}
