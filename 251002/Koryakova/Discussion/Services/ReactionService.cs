using AutoMapper;
using Shared.Models.DTOs.Requests;
using Shared.Models.DTOs.Responses;
using Discussion.Models.Entities;
using Shared.Models.Queries;
using Discussion.Interfaces;
using Discussion.Storage;
using MongoDB.Driver;
using MongoDB.Bson;
using Confluent.Kafka;
using System.Text.Json;

namespace Discussion.Services
{
    public class ReactionService
    {
        private readonly ICrudRepository<Reaction> _reactionRepository;
        private readonly IMapper _mapper;
        //private readonly IProducer<string, string> _kafkaProducer;

        public ReactionService(IMapper mapper, ICrudRepository<Reaction> reactionRepository)//, IProducer<string, string> kafkaProducer)
        {
            _reactionRepository = reactionRepository;
            _mapper = mapper;
           // _kafkaProducer = kafkaProducer;
        }

        public ReactionResponseTo CreateReaction(ReactionRequestTo request)
        {
            // 1. Input validation
            if (request == null)
                throw new ArgumentNullException(nameof(request), "Request cannot be null");

            if (request.NewsId <= 0)
                throw new ArgumentException("News ID must be positive", nameof(request.NewsId));

            // FK validation!

            try
            {
                // 2. Map to entity (AutoMapper handles property assignments)
                var reaction = _mapper.Map<Reaction>(request);
                
                // 3. Store in MongoDB (ID auto-generated if not set)
                var created = _reactionRepository.Add(reaction);

                // 4. Map back to DTO
                return _mapper.Map<ReactionResponseTo>(created);
            }
            catch (MongoWriteException ex) when (ex.WriteError.Category == ServerErrorCategory.DuplicateKey)
            {
                throw new InvalidOperationException("Reaction with this ID already exists", ex);
            }
            catch (AutoMapperMappingException ex)
            {
                throw new InvalidOperationException("Mapping failed", ex);
            }
            catch (Exception ex) when (ex is ArgumentException or ArgumentNullException)
            {
                throw; // Re-throw known exception types
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Creation failed", ex);
            }
        }

        public ReactionResponseTo GetReactionById(string id)
        {
            // Validate ID format (supports both MongoDB ObjectId and numeric strings)
            if (string.IsNullOrWhiteSpace(id) ||
                (!ObjectId.TryParse(id, out _) && !long.TryParse(id, out _)))
            {
                throw new ArgumentException(
                    "ID must be a valid MongoDB ObjectId or numeric string",
                    nameof(id));
            }

            try
            {
                var reaction = _reactionRepository.GetById(id);

                if (reaction == null)
                    throw new KeyNotFoundException($"Reaction {id} not found");

                return _mapper.Map<ReactionResponseTo>(reaction);
            }
            catch (ArgumentException)
            {
                throw; // Re-throw validation errors
            }
            catch (KeyNotFoundException)
            {
                throw; // Re-throw not found
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Failed to fetch reaction {id}", ex);
            }
        }

        public IEnumerable<ReactionResponseTo> GetAllReactions(QueryOptions<Reaction>? options = null)
        {
            try
            {
                IEnumerable<Reaction> reactions;

                if (options != null)
                {
                    // MongoDB-friendly filtered query
                    reactions = _reactionRepository.GetFiltered(options);
                }
                else
                {
                    // Default: Get all with server-side paging safeguard
                    reactions = _reactionRepository.GetAll().Take(1000); // Prevent OOM
                }

                return _mapper.Map<IEnumerable<ReactionResponseTo>>(reactions);
            }
            catch (MongoQueryException ex)
            {
                throw new InvalidOperationException("Query execution failed", ex);
            }
            catch (AutoMapperMappingException ex)
            {
                throw new InvalidOperationException("Data mapping failed", ex);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to get all reactions", ex);
            }
        }

        public ReactionResponseTo UpdateReaction(ReactionRequestTo request)
        {
            // Input validation
            if (request == null)
                throw new ArgumentNullException(nameof(request));

            if (string.IsNullOrEmpty(request.Id))
                throw new ArgumentException("Reaction ID is required", nameof(request.Id));

            try
            {
                // 1. Verify existence
                var existing = _reactionRepository.GetById(request.Id);
                if (existing == null)
                    throw new KeyNotFoundException($"Reaction {request.Id} not found");

                // 2. Map updates (preserve MongoDB-specific fields)
                _mapper.Map(request, existing);
               
                // 3. Synchronous update
                var updated = _reactionRepository.Update(existing);
                return _mapper.Map<ReactionResponseTo>(updated);
            }
            catch (MongoWriteException ex) when (ex.WriteError.Category == ServerErrorCategory.DuplicateKey)
            {
                throw new InvalidOperationException("Duplicate key violation", ex);
            }
            catch (KeyNotFoundException)
            {
                throw; // Re-throw not-found errors
            }
            catch (Exception ex) when (ex is ArgumentException or ArgumentNullException)
            {
                throw; // Re-throw validation errors
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Reaction update failed", ex);
            }
        }

        public bool DeleteReaction(string id)
        {
            // Validate ID format (supports both ObjectId and numeric strings)
            if (string.IsNullOrWhiteSpace(id) ||
                (!ObjectId.TryParse(id, out _) && !long.TryParse(id, out _)))
            {
                throw new ArgumentException(
                    "ID must be a valid MongoDB ObjectId or numeric string",
                    nameof(id));
            }

            try
            {
                return _reactionRepository.DeleteById(id);
            }
            catch (MongoWriteException ex) when (ex.WriteError.Category == ServerErrorCategory.DuplicateKey)
            {
                throw new InvalidOperationException("Concurrency conflict", ex);
            }
            catch (ArgumentException)
            {
                throw; // Re-throw validation errors
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Failed to delete reaction {id}", ex);
            }
        }

        public IEnumerable<ReactionResponseTo> GetReactionsByNewsId(long newsId)
        {
            if (newsId <= 0)
                throw new ArgumentException("News ID must be positive", nameof(newsId));

            try
            {
                var reactions = _reactionRepository.GetFiltered(
                new QueryOptions<Reaction>
                {
                    Filter = r => r.NewsId == newsId,
                    OrderBy = q => q.OrderByDescending(r => r.Id)
                });

                return _mapper.Map<IEnumerable<ReactionResponseTo>>(reactions);
            }
            catch (MongoQueryException ex)
            {
                throw new InvalidOperationException("Query execution failed", ex);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to get reaction by newsId", ex);
            }
        }

        //------------------------------ASYNC---------------------------------
        private void ValidateReactionId(string id)
        {
            if (string.IsNullOrWhiteSpace(id))
                throw new ArgumentException("ID cannot be empty");

            if (!ObjectId.TryParse(id, out _))
                throw new ArgumentException("Invalid ObjectId format");
        }

        public async Task<ReactionResponseTo> CreateReactionAsync(ReactionRequestTo request)
        {
            if (request == null) throw new ArgumentNullException(nameof(request));
            if (request.NewsId <= 0) throw new ArgumentException("News ID must be positive");

            try
            {
                var reaction = _mapper.Map<Reaction>(request);
                var created = await _reactionRepository.AddAsync(reaction);
                return _mapper.Map<ReactionResponseTo>(created);
            }
            catch (MongoWriteException ex) when (ex.WriteError.Category == ServerErrorCategory.DuplicateKey)
            {
                throw new InvalidOperationException("Reaction with this ID already exists", ex);
            }
            catch (Exception ex) when (ex is ArgumentException or AutoMapperMappingException)
            {
                throw; // Re-throw known exceptions
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Creation failed", ex);
            }
        }

        public async Task<ReactionResponseTo> UpdateReactionAsync(ReactionRequestTo request)
        {
            if (request == null) throw new ArgumentNullException(nameof(request));
            if (string.IsNullOrEmpty(request.Id)) throw new ArgumentException("Reaction ID required");

            try
            {
                var existing = await _reactionRepository.GetByIdAsync(request.Id)
                    ?? throw new KeyNotFoundException($"Reaction {request.Id} not found");

                _mapper.Map(request, existing);
                var updated = await _reactionRepository.UpdateAsync(existing);
                return _mapper.Map<ReactionResponseTo>(updated);
            }
            catch (MongoWriteException ex) when (ex.WriteError.Category == ServerErrorCategory.DuplicateKey)
            {
                throw new InvalidOperationException("Duplicate key violation", ex);
            }
            catch (Exception ex) when (ex is ArgumentException or KeyNotFoundException)
            {
                throw;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Update failed", ex);
            }
        }

        public async Task<bool> DeleteReactionAsync(string id)
        {
            ValidateReactionId(id);

            try
            {
                var deleted = await _reactionRepository.DeleteByIdAsync(id);
                /*if (deleted)
                {
                    // Publish deletion event
                    await _kafkaProducer.ProduceAsync("reaction_events",
                        new Message<string, string>
                        {
                            Key = id,
                            Value = JsonSerializer.Serialize(new
                            {
                                EventType = "deleted",
                                ReactionId = id
                            })
                        });
                }*/
                return deleted;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Delete failed: {ex.Message}", ex);
            }
        }

        public async Task<ReactionResponseTo> GetReactionByIdAsync(string id)
        {
            ValidateReactionId(id);

            var reaction = await _reactionRepository.GetByIdAsync(id)
                ?? throw new KeyNotFoundException($"Reaction {id} not found");

            return _mapper.Map<ReactionResponseTo>(reaction);
        }

        public async Task<IEnumerable<ReactionResponseTo>> GetAllReactionsAsync(
    QueryOptions<Reaction>? options = null)
    //CancellationToken ct = default)
        {
            try
            {
                IEnumerable<Reaction> reactions = options != null
                    ? await _reactionRepository.GetFilteredAsync(options)//, ct)
                    : (await _reactionRepository.GetAllAsync()).Take(1000); // Safety limit

                return _mapper.Map<IEnumerable<ReactionResponseTo>>(reactions);
            }
            catch (MongoQueryException ex)
            {
                throw new InvalidOperationException("Query failed", ex);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Failed to get reactions", ex);
            }
        }

        public async Task<IEnumerable<ReactionResponseTo>> GetReactionsByNewsIdAsync(long newsId)
        {
            if (newsId <= 0) throw new ArgumentException("News ID must be positive");

            var reactions = await _reactionRepository.GetFilteredAsync(
                new QueryOptions<Reaction>
                {
                    Filter = r => r.NewsId == newsId,
                    OrderBy = q => q.OrderByDescending(r => r.Id)
                });

            return _mapper.Map<IEnumerable<ReactionResponseTo>>(reactions);
        }
    }
}
