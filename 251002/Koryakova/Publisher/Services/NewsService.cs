using AutoMapper;
using Microsoft.EntityFrameworkCore;
using Publisher.Data;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entities;
using Shared.Models.Queries;
using Publisher.Interfaces;
using Publisher.Storage;

namespace Publisher.Services
{
    public class NewsService
    {
        private readonly ICrudRepository<News> _newsRepository;
        private readonly IMapper _mapper;
        private readonly AppDbContext _appDbContext;

        public NewsService(IMapper mapper, ICrudRepository<News> newsRepository, EditorRepository editorRepository, AppDbContext appDbContext)
        {
            _mapper = mapper;
            _newsRepository = newsRepository;
            _appDbContext = appDbContext;
        }

        public NewsResponseTo CreateNews(NewsRequestTo newsRequestTo)
        {
            if (newsRequestTo == null)
            {
                throw new ArgumentNullException(nameof(newsRequestTo), "News request cannot be null");
            }
            
            if (newsRequestTo.EditorId == null || newsRequestTo.EditorId <= 0)
            {
                throw new ArgumentException("Editor ID must be provided on news creation", nameof(newsRequestTo.EditorId));
            }

            if (_appDbContext.News.Any(n => n.Title == newsRequestTo.Title))
            {
                throw new InvalidOperationException("Duplicate title"); // Will be converted to 403
            }

            if (!_appDbContext.Editors.Any(e => e.Id == newsRequestTo.EditorId))
            {
                throw new ArgumentException($"Editor {newsRequestTo.EditorId} doesn't exist");
            }

            try
            {
                var news = _mapper.Map<News>(newsRequestTo);
                var createdNews = _newsRepository.Add(news);
                return _mapper.Map<NewsResponseTo>(createdNews);
            }
            catch (DbUpdateException dbEx)
            {
                throw new InvalidOperationException(
                    dbEx.InnerException?.Message ?? "Database error occurred while creating news",
                    dbEx);
            }
            catch (AutoMapperMappingException mapEx)
            {
                throw new InvalidOperationException("Mapping failed", mapEx);
            }
            catch (ArgumentException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to retrieve news", ex);
            }
        }

        public NewsResponseTo GetNewsById(long id)
        {
            if (id <= 0)
                throw new ArgumentException("ID must be positive", nameof(id));

            try
            {
                var news = _newsRepository.GetById(id);
                return _mapper.Map<NewsResponseTo>(news);
            }
            catch(ArgumentException)
            {
                throw;
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException($"Failed to get news by id {id}", ex);
            }
        }

        public IEnumerable<NewsResponseTo> GetAllNews(QueryOptions<News>? options = null)
        {
            try
            {
                var news = options != null ? _newsRepository.GetFiltered(options) : _newsRepository.GetAll();
                return _mapper.Map<IEnumerable<NewsResponseTo>>(news);
            }
            catch(Exception ex)
            {
                throw new InvalidOperationException("Failed to get all news.", ex);
            }
        }

        public NewsResponseTo UpdateNews(NewsRequestTo newsRequestTo)
        {
            if (newsRequestTo == null)
            {
                throw new ArgumentNullException(nameof(newsRequestTo), "Request cannot be null");
            }

            if (newsRequestTo.Id == null || newsRequestTo.Id <= 0)
            {
                throw new ArgumentException("Invalid news ID", nameof(newsRequestTo.Id));
            }

            try
            {
                var existingNews = _newsRepository.GetById(newsRequestTo.Id.Value);
                if (existingNews == null)
                {
                    throw new InvalidOperationException($"News {newsRequestTo.Id} not found");
                }

                _mapper.Map(newsRequestTo, existingNews);
                var updatedNews = _newsRepository.Update(existingNews);
                return _mapper.Map<NewsResponseTo>(updatedNews);
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
                throw new InvalidOperationException("Failed to update news", ex);
            }
        }

        public bool DeleteNews(long id)
        {
            if (id <= 0)
            {
                throw new ArgumentException("ID must be positive", nameof(id));
            }
            try
            {
                return _newsRepository.DeleteById(id);
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
                throw new InvalidOperationException("Failed to delete news", ex);
            }
        }

        public IEnumerable<NewsResponseTo> GetNewsByStickerName(string stickerName)
        {
            if (string.IsNullOrWhiteSpace(stickerName))
                throw new ArgumentException("Sticker name cannot be empty");

            try
            {
                // Step 1: Get sticker IDs matching the name
                var stickerIds = _appDbContext.Stickers
                    .Where(s => s.Name.Contains(stickerName))
                    .Select(s => s.Id)
                    .ToList();

                if (!stickerIds.Any())
                    return Enumerable.Empty<NewsResponseTo>();

                // Step 2: Get news IDs linked to these stickers
                var newsIds = _appDbContext.NewsStickers
                    .Where(ns => stickerIds.Contains(ns.StickerId))
                    .Select(ns => ns.NewsId)
                    .Distinct()
                    .ToList();

                // Step 3: Get full news details
                var newsItems = _appDbContext.News
                    .Where(n => newsIds.Contains(n.Id))
                    .ToList();

                return _mapper.Map<IEnumerable<NewsResponseTo>>(newsItems);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("News search failed", ex);
            }
        }

        public IEnumerable<NewsResponseTo> GetNewsByStickerId(long stickerId)
        {
            if (stickerId <= 0)
                throw new ArgumentException("Invalid Sticker ID");

            try
            {
                var newsIds = _appDbContext.NewsStickers
                    .Where(ns => ns.StickerId == stickerId)
                    .Select(ns => ns.NewsId)
                    .ToList();

                var news = _appDbContext.News
                    .Where(n => newsIds.Contains(n.Id))
                    .ToList();

                return _mapper.Map<IEnumerable<NewsResponseTo>>(news);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("News search failed", ex);
            }
        }

        public IEnumerable<NewsResponseTo> GetNewsByEditorLogin(string editorLogin)
        {
            if (string.IsNullOrWhiteSpace(editorLogin))
                throw new ArgumentException("Editor login cannot be empty");

            try
            {
                var editorId = _appDbContext.Editors
                    .Where(e => e.Login == editorLogin)
                    .Select(e => e.Id)
                    .FirstOrDefault();

                if (editorId == default)
                    return Enumerable.Empty<NewsResponseTo>();

                var news = _appDbContext.News
                    .Where(n => n.EditorId == editorId)
                    .ToList();

                return _mapper.Map<IEnumerable<NewsResponseTo>>(news);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("News search failed", ex);
            }
        }

        public IEnumerable<NewsResponseTo> SearchNews(NewsSearchRequestDto searchRequest)
        {
            // Базовый запрос
            IQueryable<News> query = _appDbContext.News;

            // Фильтр по заголовку
            if (!string.IsNullOrWhiteSpace(searchRequest.Title))
            {
                query = query.Where(n => n.Title.Contains(searchRequest.Title));
            }

            // Фильтр по содержимому
            if (!string.IsNullOrWhiteSpace(searchRequest.Content))
            {
                query = query.Where(n => n.Content.Contains(searchRequest.Content));
            }

            // Фильтр по логину редактора
            if (!string.IsNullOrWhiteSpace(searchRequest.EditorLogin))
            {
                query = query
                .Join(_appDbContext.Editors,
                    news => news.EditorId,
                    editor => editor.Id,
                    (news, editor) => new { News = news, Editor = editor })
                .Where(x => x.Editor.Login == searchRequest.EditorLogin)
                .Select(x => x.News);
            }

            // Фильтр по ID стикера
            if (searchRequest.StickerId.HasValue)
            {
                var newsIds = _appDbContext.NewsStickers
                    .Where(ns => ns.StickerId == searchRequest.StickerId)
                    .Select(ns => ns.NewsId);
                query = query.Where(n => newsIds.Contains(n.Id));
            }

            // Фильтр по названию стикера
            if (!string.IsNullOrWhiteSpace(searchRequest.StickerName))
            {
                var stickerIds = _appDbContext.Stickers
                    .Where(s => s.Name.Contains(searchRequest.StickerName))
                    .Select(s => s.Id);
                var newsIds = _appDbContext.NewsStickers
                    .Where(ns => stickerIds.Contains(ns.StickerId))
                    .Select(ns => ns.NewsId);
                query = query.Where(n => newsIds.Contains(n.Id));
            }

            // Выполняем запрос
            var result = query.ToList();
            return _mapper.Map<IEnumerable<NewsResponseTo>>(result);
        }
    }
}
