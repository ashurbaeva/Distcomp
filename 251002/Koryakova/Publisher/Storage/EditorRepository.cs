using Publisher.Models.DTOs.Requests;
using Microsoft.EntityFrameworkCore;
using Publisher.Models.Entities;
using Publisher.Data;
using Shared.Models.Queries;
using Publisher.Interfaces;

namespace Publisher.Storage
{
    public class EditorRepository : ICrudRepository<Editor>
    {
        private readonly DbSet<Editor> _dbSet;
        private readonly AppDbContext _appDbContext;

        public EditorRepository(AppDbContext appDbContext)
        {
            _appDbContext = appDbContext;
            _dbSet  = appDbContext.Set<Editor>();
        }

        public Editor? GetById(long id)
        {
            if(id <= 0)
            {
                throw new ArgumentException("ID must be positive.", nameof(id));
            }

            return _dbSet.Find(id);
        }
         // eQueriable
        public IEnumerable<Editor> GetAll()
        {
            return _dbSet.ToList();
        }

        public Editor Add(Editor editor)
        {
            if (editor == null)
            {
                throw new ArgumentNullException(nameof(editor), "Editor cannot be null.");
            }
           /* if (editor.Id <= 0)
            {
                throw new ArgumentException("Editor ID must be positive.", nameof(editor.Id));
            }
            if (editor.Id != 0 && _dbSet.Any(e => e.Id == editor.Id))
            {
                throw new InvalidOperationException($"Editor with ID {editor.Id} already exists.");
            }*/

            try
            {
                _dbSet.Add(editor);
                _appDbContext.SaveChanges();
                return editor;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException(ex.Message + " Could not add the editor.");
            }
        }

        public Editor Update(Editor editor)
        {
            if(editor == null)
            {
                throw new ArgumentNullException(nameof(editor), "Editor cannot be null.");
            }
            if (editor.Id <= 0)
            {
                throw new ArgumentException("Editor ID must be positive.", nameof(editor.Id));
            }

            try
            {
                var existing = _dbSet.Find(editor.Id);
                if(existing == null)
                {
                    throw new InvalidOperationException($"Editor with ID {editor.Id} not found");
                }

                _appDbContext.Entry(existing).CurrentValues.SetValues(editor);
                _appDbContext.SaveChanges();
                return existing;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException(ex.Message + " Could not update the editor.");
            }
        }

        public bool DeleteById(long id)
        {
            if(id <= 0)
            {
                throw new ArgumentException("ID must be positive.", nameof(id));
            }

            try
            {
                var editor = _dbSet.Find(id);
                if(editor == null)
                {
                    return false;
                }

                _dbSet.Remove(editor);
                _appDbContext.SaveChanges();
                return true;
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException(ex.Message + " Could not delete the editor.");
            }
        }

        public IEnumerable<Editor> GetFiltered(QueryOptions<Editor> options)
        {
            IQueryable<Editor> query = _dbSet;

            if (options.Filter != null)
            {
                query = query.Where(options.Filter);
            }

            query = options.OrderBy != null ? options.OrderBy(query) : query.OrderBy(e => e.Id);

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
