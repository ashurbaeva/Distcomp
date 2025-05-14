using DistComp.Models;

namespace DistComp.Repositories.Interfaces;

public interface ILabelRepository : IRepository<Label>
{
    Task<IEnumerable<Label>> GetByNamesAsync(IEnumerable<string> names);

}