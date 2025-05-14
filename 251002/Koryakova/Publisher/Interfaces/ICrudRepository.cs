using Shared.Models.Queries;

namespace Publisher.Interfaces
{
    public interface ICrudRepository<T> where T : class
    {
        T? GetById(long id);
        IEnumerable<T> GetAll();
        T Add(T entity);
        T Update(T entity);
        bool DeleteById(long id);
        IEnumerable<T> GetFiltered(QueryOptions<T> options);
    }
}
