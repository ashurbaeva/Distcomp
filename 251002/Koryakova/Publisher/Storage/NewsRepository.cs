using Microsoft.AspNetCore.Components.Forms;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using Publisher.Data;
using Publisher.Models.Entities;
using Shared.Models.Queries;
using Publisher.Interfaces;

namespace Publisher.Storage
{
    public class NewsRepository : ICrudRepository<News>
    {
        private readonly DbSet<News> _dbSet;
        private readonly AppDbContext _appDbContext;

        public NewsRepository(AppDbContext appDbContext)
        {
            _appDbContext = appDbContext;
            _dbSet = appDbContext.Set<News>();
        }

        public News? GetById(long id)
        {
            if (id <= 0)
            {
                throw new ArgumentException("ID must be positive.", nameof(id));
            }

            return _dbSet.Find(id);
        }

        public IEnumerable<News> GetAll()
        {
            return _dbSet.ToList();
        }

        public News Add(News news)
        {
            if (news == null)
            {
                throw new ArgumentNullException(nameof(news), "News cannot be null.");
            }
            
            if (news.Id != 0 && _dbSet.Any(n => n.Id == news.Id))
            {
                throw new InvalidOperationException($"News with ID {news.Id} already exists.");
            }
            
            if (news.Id < 0)
            {
                throw new ArgumentException("News ID must be non-negative.", nameof(news.Id));
            }
            
            if (news.EditorId <= 0)
            { 
                throw new ArgumentException("Editor ID must be positive.", nameof(news.EditorId));
            }

            try
            {
                _dbSet.Add(news);
                _appDbContext.SaveChanges();
                return news;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Could not add the news: " + ex.Message);
            }
        }

        public News Update(News news)
        {
            if (news == null)
            {
                throw new ArgumentNullException(nameof(news), "News cannot be null.");
            }

            if (news.Id <= 0)
            {
                throw new ArgumentException("News ID must be positive.", nameof(news.Id));
            }
                        
            if (news.EditorId < 0)
            {
                throw new ArgumentException("Editor ID must be non-negative.", nameof(news.EditorId));
            }
            try
            {
                var existing = _dbSet.Find(news.Id);
                
                if (existing == null)
                {
                    throw new InvalidOperationException($"News with ID {news.Id} not found.");
                }

                if (news.EditorId == null && existing.EditorId != null)
                {
                    throw new InvalidOperationException("Forbiddden to set EditorId to null.");
                }

                _appDbContext.Entry(existing).CurrentValues.SetValues(news);
                _appDbContext.SaveChanges();
                return existing;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Could not update the news: " + ex.Message);
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
                var news = _dbSet.Find(id);
                if (news == null)
                {
                    return false;
                }

                _dbSet.Remove(news);
                _appDbContext.SaveChanges();
                return true;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Could not delete the news: " + ex.Message);
            }
        }

        public IEnumerable<News> GetFiltered(QueryOptions<News> options)
        {
            IQueryable<News> query = _dbSet;

            if (options.Filter != null)
            {
                query = query.Where(options.Filter);
            }

            query = options.OrderBy != null ? options.OrderBy(query) : query.OrderBy(n => n.Created);

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