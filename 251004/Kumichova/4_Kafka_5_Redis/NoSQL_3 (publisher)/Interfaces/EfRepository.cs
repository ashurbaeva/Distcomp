using LAB2.Data;
using LAB2.Domain;
using Microsoft.EntityFrameworkCore;

namespace LAB2.Interfaces;

public class EfRepository<T> : IRepository<T> where T : Entity
{
    private readonly AppDbContext _context;
    private readonly DbSet<T> _dbSet;

    public EfRepository(AppDbContext context)
    {
        _context = context;
        _dbSet = context.Set<T>();
    }

    public async Task<T?> GetByIdAsync(int id) => await _dbSet.FindAsync(id);

    public async Task<IEnumerable<T>> GetAllAsync(QueryParams? queryParams = null)
    {
        queryParams ??= new QueryParams();
        var query = _dbSet.AsQueryable();

        if (!string.IsNullOrEmpty(queryParams.SortBy))
        {
            query = queryParams.SortDesc 
                ? query.OrderByDescending(e => EF.Property<object>(e, queryParams.SortBy))
                : query.OrderBy(e => EF.Property<object>(e, queryParams.SortBy));
        }

        return await query
            .Skip((queryParams.PageNumber - 1) * queryParams.PageSize)
            .Take(queryParams.PageSize)
            .ToListAsync();
    }

    public async Task<T> CreateAsync(T entity)
    {
        await _dbSet.AddAsync(entity);
        await _context.SaveChangesAsync();
        return entity;
    }

    public async Task<T> UpdateAsync(T entity)
    {
        _dbSet.Update(entity);
        await _context.SaveChangesAsync();
        return entity;
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await GetByIdAsync(id);
        if (entity == null) return false;
            
        _dbSet.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }

    public IQueryable<T> GetQueryable() => _dbSet.AsQueryable();
}