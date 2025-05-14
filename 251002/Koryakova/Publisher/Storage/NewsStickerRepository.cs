using Microsoft.EntityFrameworkCore;
using Publisher.Data;
using Publisher.Models.Entities;
using Publisher.Interfaces;

namespace Publisher.Storage
{
    public class NewsStickerRepository
    {
        private readonly DbSet<NewsSticker> _dbSet;
        private readonly AppDbContext _appDbContext;

        public NewsStickerRepository(AppDbContext appDbContext)
        {
            _appDbContext = appDbContext;
            _dbSet = appDbContext.Set<NewsSticker>();
        }

        public bool Exists(long newsId, long stickerId)
        {
            return _dbSet.Any(r => r.NewsId == newsId && r.StickerId == stickerId);
        }

        public bool RemoveByNewsId(long newsId)
        {
            var relations = _dbSet
            .Where(r => r.NewsId == newsId)
            .ToList();

            if (relations.Any())
            {
                _dbSet.RemoveRange(relations);
                return _appDbContext.SaveChanges() > 0;
            }
            return false;
        }

        public bool RemoveByStickerId(long stickerId)
        {
            var relations = _dbSet
            .Where(r => r.StickerId == stickerId)
            .ToList();

            if (relations.Any())
            {
                _dbSet.RemoveRange(relations);
                return _appDbContext.SaveChanges() > 0;
            }
            return false;
        }

        public void Add(long newsId, long stickerId)
        {
            if (!Exists(newsId, stickerId))
            {
                _dbSet.Add(new NewsSticker
                {
                    NewsId = newsId,
                    StickerId = stickerId
                });
                _appDbContext.SaveChanges();
            }
        }

        public bool Remove(long newsId, long stickerId)
        {
            var relation = _dbSet.FirstOrDefault(r =>
                r.NewsId == newsId && r.StickerId == stickerId);

            if (relation != null)
            {
                _dbSet.Remove(relation);
                return _appDbContext.SaveChanges() > 0;
            }
            return false;
        }

        public List<long> GetStickersForNews(long newsId)
        {
            return _dbSet
            .Where(r => r.NewsId == newsId)
            .Select(r => r.StickerId)
            .ToList();
        }

        public List<long> GetNewsForSticker(long stickerId)
        {
            return _dbSet
            .Where(r => r.StickerId == stickerId)
            .Select(r => r.NewsId)
            .ToList();
        }

        public int CountRelationsForNews(long newsId)
        {
            return _dbSet
                .Count(r => r.NewsId == newsId);
        }

        public int CountRelationsForSticker(long stickerId)
        {
            return _dbSet
                .Count(r => r.StickerId == stickerId);
        }
    }
}
