using Publisher.Data;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace Publisher.Repositories.Implementations;

public class DatabaseLabelRepository : BaseDatabaseRepository<Label>, ILabelRepository
{
    public DatabaseLabelRepository(AppDbContext context) : base(context)
    {
    }
    
    public async Task<IEnumerable<Label>> GetByNamesAsync(IEnumerable<string> names)
    {
        return await _context.Marks
            .Where(m => names.Contains(m.Name))
            .ToListAsync();
    }
}