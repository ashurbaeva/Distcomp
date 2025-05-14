using LabsRV_Articles.Data;
using LabsRV_Articles.Models.Domain;
using Microsoft.EntityFrameworkCore;


namespace LabsRV_Articles.Repositories
{
    public class Repository<T> : IRepository<T> where T : class, IEntity
    {
        protected readonly ApplicationDbContext _context;
        protected readonly DbSet<T> _dbSet;

        public Repository(ApplicationDbContext context)
        {
            _context = context;
            _dbSet = context.Set<T>();
        }

        public async Task<T> AddAsync(T entity)
        {
            await _dbSet.AddAsync(entity);
            await _context.SaveChangesAsync();
            return entity;
        }

        public T Add(T entity)
        {
            _dbSet.Add(entity);
            _context.SaveChanges();
            return entity;
        }

        public async Task<bool> DeleteAsync(int id)
        {
            var entity = await _dbSet.FindAsync(id);
            if (entity == null) return false;
            _dbSet.Remove(entity);
            await _context.SaveChangesAsync();
            return true;
        }

        public bool Delete(int id)
        {
            var entity = _dbSet.Find(id);
            if (entity == null) return false;
            _dbSet.Remove(entity);
            _context.SaveChanges();
            return true;
        }

        public async Task<IEnumerable<T>> GetAllAsync()
        {
            return await _dbSet.ToListAsync();
        }

        public IEnumerable<T> GetAll()
        {
            return _dbSet.ToList();
        }

        public async Task<T?> GetByIdAsync(int id)
        {
            return await _dbSet.FindAsync(id);
        }

        public T? GetById(int id)
        {
            return _dbSet.Find(id);
        }

        // Реализация простейшей пагинации, сортировки и фильтрации
        public async Task<(IEnumerable<T> Items, int TotalCount)> GetPagedAsync(int page, int pageSize, string sort, string filter)
        {
            IQueryable<T> query = _dbSet;

            if (!string.IsNullOrWhiteSpace(filter))
                query = query.Where(e => EF.Functions.ILike(EF.Property<string>(e, "Id"), $"%{filter}%"));

            if (!string.IsNullOrWhiteSpace(sort))
            {
                var sortParams = sort.Split(' ');
                var sortField = sortParams[0];
                var sortOrder = sortParams.Length > 1 && sortParams[1].ToLower() == "desc" ? "desc" : "asc";
                query = sortOrder == "desc"
                    ? query.OrderByDescending(e => EF.Property<object>(e, sortField))
                    : query.OrderBy(e => EF.Property<object>(e, sortField));
            }
            else
            {
                query = query.OrderBy(e => e.id);
            }

            int totalCount = await query.CountAsync();
            var items = await query.Skip((page - 1) * pageSize).Take(pageSize).ToListAsync();

            return (items, totalCount);
        }

        public async Task<T> UpdateAsync(T entity)
        {
            _dbSet.Update(entity);
            await _context.SaveChangesAsync();
            return entity;
        }

        public T Update(T entity)
        {
            _dbSet.Update(entity);
            _context.SaveChanges();
            return entity;
        }
    }
}
