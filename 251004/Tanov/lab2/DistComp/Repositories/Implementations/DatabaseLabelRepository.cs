using DistComp.Data;
using DistComp.Models;
using DistComp.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace DistComp.Repositories.Implementations;

public class DatabaseLabelRepository : BaseDatabaseRepository<Label>, ILabelRepository
{
    public DatabaseLabelRepository(AppDbContext context) : base(context)
    {
    }
    
    public async Task<IEnumerable<Label>> GetByNamesAsync(IEnumerable<string> names)
    {
        return await _context.Labels
            .Where(m => names.Contains(m.Name))
            .ToListAsync();
    }
}