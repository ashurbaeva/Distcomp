using Microsoft.EntityFrameworkCore;
using System.Linq;
using Publisher.Data;
using Publisher.Models.Entities;
using Shared.Models.Queries;
using Publisher.Interfaces;

namespace Publisher.Storage
{
    public class StickerRepository : ICrudRepository<Sticker>
    {
        private readonly DbSet<Sticker> _dbSet;
        private readonly AppDbContext _appDbContext;

        public StickerRepository(AppDbContext appDbContext)
        {
            _appDbContext = appDbContext;
            _dbSet = appDbContext.Set<Sticker>();
        }

        public Sticker? GetById(long id)
        {
            if (id <= 0)
            {
                throw new ArgumentException("ID must be positive.", nameof(id));
            }

            return _dbSet.Find(id);
        }

        public IEnumerable<Sticker> GetAll()
        {
            return _dbSet.ToList();
        }

        public Sticker Add(Sticker  sticker)
        {
            if (sticker == null)
            {
                throw new ArgumentNullException(nameof(sticker), "Sticker cannot be null.");
            }

            if (sticker.Id != 0 && _dbSet.Any(s => s.Id == sticker.Id))
            {
                throw new InvalidOperationException($"Sticker with ID {sticker.Id} already exists.");
            }

            if (sticker.Id < 0)
            {
                throw new ArgumentException("Sticker ID must be non-negative.", nameof(sticker.Id));
            }

            if (_dbSet.Any(s => s.Name == sticker.Name))
            {
                throw new InvalidOperationException($"Sticker with name '{sticker.Name}' already exists.");
            }

            try
            {
                _dbSet.Add(sticker);
                _appDbContext.SaveChanges();
                return sticker;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Could not add the sticker: " + ex.Message);
            }
        }

        public Sticker Update(Sticker sticker)
        {
            if (sticker == null)
            {
                throw new ArgumentNullException(nameof(sticker), "Sticker cannot be null.");
            }

            if (sticker.Id <= 0)
            {
                throw new ArgumentException("Sticker ID must be positive.", nameof(sticker.Id));
            }

            try
            {
                var existing = _dbSet.Find(sticker.Id);

                if (existing == null)
                {
                    throw new InvalidOperationException($"Sticker with ID {sticker.Id} not found.");
                }

                if (_dbSet.Any(s => s.Name == sticker.Name && s.Id != sticker.Id))
                {
                    throw new InvalidOperationException($"Sticker with name '{sticker.Name}' already exists.");
                }

                _appDbContext.Entry(existing).CurrentValues.SetValues(sticker);
                _appDbContext.SaveChanges();
                return existing;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Could not update the sticker: " + ex.Message);
            }
        }

        public bool DeleteById(long id)
        {
            if (id <= 0)
            {
                throw new ArgumentException("ID must be positive.", nameof(id));
            }

            try
            {
                var sticker = _dbSet.Find(id);
                if (sticker == null)
                {
                    return false;
                }

                _dbSet.Remove(sticker);
                _appDbContext.SaveChanges();
                return true;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Could not delete the sticker: " + ex.Message);
            }
        }

        public IEnumerable<Sticker> GetFiltered(QueryOptions<Sticker> options)
        {
            IQueryable<Sticker> query = _dbSet;

            if (options.Filter != null)
            {
                query = query.Where(options.Filter);
            }

            query = options.OrderBy != null ? options.OrderBy(query) : query.OrderBy(s => s.Id);

            if (options.PageNumber.HasValue && options.PageSize.HasValue)
            {
                query = query.Skip((options.PageNumber.Value - 1) * options.PageSize.Value)
                    .Take(options.PageSize.Value);
            }

            foreach (var includeProperty in options.IncludeProperties)
            {
                query = query.Include(includeProperty);
            }

            return query.ToList();
        }
    }
}