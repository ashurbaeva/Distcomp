using Publisher.Models;

namespace Publisher.Repositories.Interfaces;

public interface ILabelRepository : IRepository<Label>
{
    Task<IEnumerable<Label>> GetByNamesAsync(IEnumerable<string> names);

}