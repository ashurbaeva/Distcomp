using System.Linq.Expressions;

namespace Shared.Models.Queries
{
    public class QueryOptions<T>
    {
        // Filtering: WHERE clauses
        public Expression<Func<T, bool>>? Filter { get; set; }

        // Sorting: ORDER BY clauses
        public Func<IQueryable<T>, IOrderedQueryable<T>>? OrderBy { get; set; }

        // Pagination
        public int? PageNumber { get; set; }
        public int? PageSize { get; set; }

        // Related data loading
        public List<string> IncludeProperties { get; set; } = new();
    }
}
