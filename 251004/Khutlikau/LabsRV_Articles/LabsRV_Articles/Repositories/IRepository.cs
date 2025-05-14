using LabsRV_Articles.Models.Domain;

namespace LabsRV_Articles.Repositories
{
    public interface IRepository<T> where T : class, IEntity
    {
        Task<T?> GetByIdAsync(int id);
        Task<IEnumerable<T>> GetAllAsync();
        Task<(IEnumerable<T> Items, int TotalCount)> GetPagedAsync(int page, int pageSize, string sort, string filter);
        Task<T> AddAsync(T entity);
        Task<T> UpdateAsync(T entity);
        Task<bool> DeleteAsync(int id);

        // Для синхронного варианта, если требуется:
        T Add(T entity);
        T Update(T entity);
        bool Delete(int id);
        T? GetById(int id);
        IEnumerable<T> GetAll();
    }
}

