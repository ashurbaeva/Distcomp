using AutoMapper;
using Microsoft.AspNetCore.Components.Forms;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using Publisher.Data;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entities;
using Shared.Models.Queries;
using Publisher.Interfaces;
using Publisher.Storage;

namespace Publisher.Services
{
    public class EditorService
    {
        private readonly ICrudRepository<Editor> _editorRepository;
        private readonly IMapper _mapper;
        private readonly AppDbContext _appDbContext;


        public EditorService(IMapper mapper, ICrudRepository<Editor> editorRepository, AppDbContext appDbContext)
        {
            _mapper = mapper;
            _editorRepository = editorRepository;
            _appDbContext = appDbContext;
        }

        public EditorResponseTo CreateEditor(EditorRequestTo editorRequestTo)
        {
            if (editorRequestTo == null)
            {
                throw new ArgumentNullException(nameof(editorRequestTo), "Editor request cannot be null");
            }
            if (_appDbContext.Editors.Any(e => e.Login == editorRequestTo.Login))
            {
                throw new InvalidOperationException("Login already exists");
            }
            try
            {
                var editor = _mapper.Map<Editor>(editorRequestTo);
                var createdEditor = _editorRepository.Add(editor);
                return _mapper.Map<EditorResponseTo>(createdEditor);
            }
            catch (DbUpdateException ex)
            {
                // Fallback duplicate check
                if (ex.InnerException is PostgresException { SqlState: "23505" })
                {
                    throw new InvalidOperationException("Login already exists");
                }
                throw;
            }
            catch (AutoMapperMappingException mapEx)
            {
                throw new InvalidOperationException("Mapping failed", mapEx);
            }
            catch (Exception ex) when (ex is ArgumentException or
                                ArgumentNullException or
                                InvalidOperationException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to create editor", ex);
            }
        }

        public EditorResponseTo? GetEditorById(long id)
        {
            if (id <= 0)
                throw new ArgumentException("ID must be positive", nameof(id));

            try
            {
                var editor = _editorRepository.GetById(id);
                return _mapper.Map<EditorResponseTo>(editor);
            }
            catch(ArgumentException)
            {
                throw;
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException("Failed to retrieve editor", ex);
            }
        }

        public IEnumerable<EditorResponseTo> GetAllEditors(QueryOptions<Editor>? options = null)
        {
            try
            {
                var editors = options != null ? _editorRepository.GetFiltered(options) : _editorRepository.GetAll();
                return _mapper.Map<IEnumerable<EditorResponseTo>>(editors);
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException("Failed to retrieve editors", ex);
            }
        }

        public EditorResponseTo UpdateEditor(EditorRequestTo editorRequestTo)
        {
            if (editorRequestTo == null)
            {
                throw new ArgumentNullException(nameof(editorRequestTo), "Request cannot be null");
            }

            if (editorRequestTo.Id == null || editorRequestTo.Id <= 0)
            {
                throw new ArgumentException("Invalid editor ID", nameof(editorRequestTo.Id));
            }

            try
            {
                var existingEditor = _editorRepository.GetById(editorRequestTo.Id.Value);
                if (existingEditor == null)
                {
                    throw new InvalidOperationException($"Editor {editorRequestTo.Id} not found");
                }

                _mapper.Map(editorRequestTo, existingEditor);
                var updatedEditor = _editorRepository.Update(existingEditor);
                return _mapper.Map<EditorResponseTo>(updatedEditor);
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (Exception ex) when (ex is ArgumentException or
                              ArgumentNullException or
                              InvalidOperationException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to update editor", ex);
            }
        }

        public bool DeleteEditor(long id)
        {
            if (id <= 0)
            {
                throw new ArgumentException("ID must be positive", nameof(id));
            }
            try
            {
                return _editorRepository.DeleteById(id);
            }
            catch (DbUpdateException ex)
            {
                throw new InvalidOperationException(ex.InnerException?.Message ?? "Database operation failed");
            }
            catch (ArgumentException)
            {
                throw;
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException("Failed to delete.", ex);
            }
        }

        public EditorResponseTo? GetEditorByNewsId(long newsId)
        {
            if (newsId <= 0)
                throw new ArgumentException("Invalid News ID");

            try
            {
                var editorId = _appDbContext.News
                .Where(n => n.Id == newsId)
                .Select(n => n.EditorId)
                .FirstOrDefault();

                if (editorId == null) return null;

                return _mapper.Map<EditorResponseTo>(
                    _editorRepository.GetById(editorId.Value)
                );
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Search failed", ex);
            }
        }
    }
}
