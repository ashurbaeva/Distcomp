using MongoDB.Driver;
using Discussion.Interfaces;
using Discussion.Models.Entities;
using Shared.Models.Queries;
using MongoDB.Bson;
using Shared.Models.DTOs.Responses;
using MongoDB.Driver.Linq;

namespace Discussion.Storage
{
    public class ReactionRepository : ICrudRepository<Reaction>
    {
        private readonly IMongoCollection<Reaction> _collection;

        public ReactionRepository(IMongoDatabase database)
        {
            _collection = database.GetCollection<Reaction>("tbl_reaction");
        }

        public Reaction? GetById(string id)
        {
            if (string.IsNullOrWhiteSpace(id) || !ObjectId.TryParse(id, out _))
            {
                throw new ArgumentException("Invalid ObjectId format.", nameof(id));
            }

            return _collection
             .Find(Builders<Reaction>.Filter.Eq(r => r.Id, id))
             .FirstOrDefault();
        }

        public IEnumerable<Reaction> GetAll()
        {
            return _collection
                .Find(FilterDefinition<Reaction>.Empty) // Gets all documents
                .ToList();
        }

        public Reaction Add(Reaction reaction)
        {
            if (reaction == null)
                throw new ArgumentNullException(nameof(reaction), "Reaction cannot be null.");

            // MongoDB uses string IDs - skip numeric ID checks
            if (!string.IsNullOrEmpty(reaction.Id))
                throw new InvalidOperationException("New reactions must not have pre-set IDs.");

            if (reaction.NewsId <= 0)
                throw new ArgumentException("News ID must be positive.", nameof(reaction.NewsId));

            // check FK!
            
            try
            {
                // MongoDB auto-generates ObjectId if Id is null/empty
                reaction.Id ??= ObjectId.GenerateNewId().ToString();

                _collection.InsertOne(reaction); // Synchronous insert
                return reaction;
            }
            catch (MongoWriteException ex) when (ex.WriteError.Category == ServerErrorCategory.DuplicateKey)
            {
                throw new InvalidOperationException($"Reaction with ID {reaction.Id} already exists.", ex);
            }
            catch (MongoException ex)
            {
                throw new InvalidOperationException("Database operation failed: " + ex.Message, ex);
            }
        }

        public Reaction Update(Reaction reaction)
        {
            if (reaction == null)
                throw new ArgumentNullException(nameof(reaction));

            if (string.IsNullOrEmpty(reaction.Id))
                throw new ArgumentException("Reaction ID must be provided.");

            if (reaction.NewsId <= 0)
                throw new ArgumentException("News ID must be positive.", nameof(reaction.NewsId));

            try
            {
                var filter = Builders<Reaction>.Filter.Eq(r => r.Id, reaction.Id);
                var options = new ReplaceOptions { IsUpsert = false };

                var result = _collection.ReplaceOne(filter, reaction, options);

                if (result.MatchedCount == 0)
                    throw new KeyNotFoundException($"Reaction with ID {reaction.Id} not found.");

                return reaction;
            }
            catch (MongoException ex)
            {
                throw new InvalidOperationException("Update failed: " + ex.Message, ex);
            }
        }

        public bool DeleteById(string id)
        {
            // Validate ID format (MongoDB ObjectId or numeric string)
            if (string.IsNullOrWhiteSpace(id) ||
                (!ObjectId.TryParse(id, out _) && !long.TryParse(id, out _)))
            {
                throw new ArgumentException("Invalid ID format. Must be ObjectId or numeric string.", nameof(id));
            }

            try
            {
                var filter = Builders<Reaction>.Filter.Eq(r => r.Id, id);
                var result = _collection.DeleteOne(filter); // Synchronous deletion

                return result.DeletedCount > 0; // True if document was found and deleted
            }
            catch (MongoException ex)
            {
                throw new InvalidOperationException("Delete operation failed: " + ex.Message, ex);
            }
        }

        public IEnumerable<Reaction> GetFiltered(QueryOptions<Reaction> options)
        {
            // Start with IQueryable from MongoDB driver's LINQ provider
            var query = _collection.AsQueryable();

            // 1. Apply filtering
            if (options.Filter != null)
            {
                query = query.Where(options.Filter);
            }

            // 2. Apply sorting
            query = options.OrderBy != null
                ? options.OrderBy(query)
                : query.OrderBy(r => r.Id);

            // 3. Apply paging
            if (options.PageNumber.HasValue && options.PageSize.HasValue)
            {
                query = query
                    .Skip((options.PageNumber.Value - 1) * options.PageSize.Value)
                    .Take(options.PageSize.Value);
            }

            // Execute synchronously
            return query.ToList();
        }

        //----------------------------ASYNC-------------------------------------
        private void ValidateId(string id)
        {
            if (string.IsNullOrWhiteSpace(id) || !ObjectId.TryParse(id, out _))
                throw new ArgumentException("Invalid ObjectId format.");
        }

        private void ValidateReaction(Reaction reaction)
        {
            if (reaction == null) throw new ArgumentNullException(nameof(reaction));
            if (reaction.NewsId <= 0) throw new ArgumentException("News ID must be positive");
        }

        public async Task<Reaction?> GetByIdAsync(string id)
        {
            ValidateId(id);
            return await _collection
                .Find(r => r.Id == id)
                .FirstOrDefaultAsync();
        }

        public async Task<IEnumerable<Reaction>> GetAllAsync()
        {
            return await _collection
                .Find(_ => true)
                .ToListAsync();
        }

        public async Task<Reaction> AddAsync(Reaction reaction)
        {
            ValidateReaction(reaction);
            reaction.Id ??= ObjectId.GenerateNewId().ToString();

            await _collection.InsertOneAsync(reaction);
            return reaction;
        }

        public async Task<Reaction> UpdateAsync(Reaction reaction)
        {
            ValidateReaction(reaction);
            var result = await _collection.ReplaceOneAsync(
                filter: r => r.Id == reaction.Id,
                replacement: reaction,
                options: new ReplaceOptions { IsUpsert = false });

            if (result.MatchedCount == 0)
                throw new KeyNotFoundException($"Reaction {reaction.Id} not found");

            return reaction;
        }

        public async Task<bool> DeleteByIdAsync(string id)
        {
            ValidateId(id);
            var result = await _collection.DeleteOneAsync(r => r.Id == id);
            return result.DeletedCount > 0;
        }

        public async Task<IEnumerable<Reaction>> GetFilteredAsync(
        QueryOptions<Reaction> options)
        {
            var query = _collection.AsQueryable();

            if (options.Filter != null)
                query = query.Where(options.Filter);

            query = options.OrderBy != null
                ? options.OrderBy(query)
                : query.OrderBy(r => r.Id);

            if (options.PageNumber.HasValue && options.PageSize.HasValue)
                query = query
                    .Skip((options.PageNumber.Value - 1) * options.PageSize.Value)
                    .Take(options.PageSize.Value);

            return await query.ToListAsync();
        }
    }
}
