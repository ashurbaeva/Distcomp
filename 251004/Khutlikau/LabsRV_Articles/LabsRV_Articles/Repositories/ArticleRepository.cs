using LabsRV_Articles.Data;
using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.MyApp.Models.Domain;
using Microsoft.EntityFrameworkCore;

namespace LabsRV_Articles.Repositories
{
    public class ArticleRepository : Repository<Article>
    {
        public ArticleRepository(ApplicationDbContext context) :
            base(context)
        { }

        public async Task<bool> UniqueTitleExistsAsync(string title)
        {
            return await _dbSet.AsNoTracking().AnyAsync(article => (article.title == title));
        }

        public async Task<List<Sticker>> GetStickersByArticleIdAsync(int articleId)
        {
            return await _context.ArticleStickers
                .Where(as_ => as_.articleId == articleId)
                .Select(as_ => as_.sticker)
                .ToListAsync();
        }

        public async Task AddStickersToArticleAsync(int articleId, List<int> stickerIds)
        {
            var article = await _dbSet
                .Include(a => a.articleStickers)
                .FirstOrDefaultAsync(a => a.id == articleId);

            if (article == null)
                throw new ArgumentException("Article not found");

            var newStickers = stickerIds
                .Select(id => new ArticleSticker
                {
                    articleId = articleId,
                    stickerId = id
                })
                .ToList();

            await _context.ArticleStickers.AddRangeAsync(newStickers);
            await _context.SaveChangesAsync();
        }
    }
}
