using LabsRV_Articles.Data;
using LabsRV_Articles.Models.Domain;
using Microsoft.EntityFrameworkCore;

namespace LabsRV_Articles.Repositories
{
    public class StickerRepository : Repository<Sticker>
    {
        public StickerRepository(ApplicationDbContext context) : base(context)
        { }

        // Метод для поиска стикеров по именам
        public async Task<List<Sticker>> GetByNamesAsync(List<string> names)
        {
            return await _dbSet
                .Where(s => names.Contains(s.name))
                .ToListAsync();
        }

        public async Task AddRangeAsync(List<Sticker> stickers)
        {
            await _dbSet.AddRangeAsync(stickers);
            await _context.SaveChangesAsync();
        }

        public async Task<bool> IsUsedByOtherArticlesAsync(int stickerId, int excludedArticleId)
        {
            return await _context.ArticleStickers
                .AnyAsync(as_ => as_.stickerId == stickerId && as_.articleId != excludedArticleId);
        }
    }
}
