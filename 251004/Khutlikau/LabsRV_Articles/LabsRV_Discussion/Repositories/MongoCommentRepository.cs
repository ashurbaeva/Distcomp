using LabsRV_Discussion.Models.Domain;
using MongoDB.Bson;
using MongoDB.Driver;
using System.Linq.Expressions;

namespace LabsRV_Discussion.Repositories
{
    public class MongoCommentRepository
    {
        private readonly IMongoCollection<Comment> _comments;
        private readonly IMongoDatabase _database; // Чтобы иметь доступ к базе, например, для счетчика

        public MongoCommentRepository(IConfiguration configuration)
        {
            var client = new MongoClient(configuration["MongoDb:ConnectionString"]);
            _database = client.GetDatabase(configuration["MongoDb:DatabaseName"]);
            _comments = _database.GetCollection<Comment>(configuration["MongoDb:CommentCollectionName"]);
        }

        public async Task<Comment> GetByIdAsync(int id)
        {
            var filter = Builders<Comment>.Filter.Eq(c => c.id, id);
            return await _comments.Find(filter).FirstOrDefaultAsync();
        }

        public async Task<IEnumerable<Comment>> GetAllAsync()
        {
            return await _comments.Find(_ => true).ToListAsync();
        }

        public async Task<Comment> AddAsync(Comment comment)
        {
            // Если вы не генерируете id на стороне клиента, то нужно установить его до вставки.
            comment.id = GetNextSequenceValue("comments");
            await _comments.InsertOneAsync(comment);
            return comment;
        }

        public async Task<Comment> UpdateAsync(Comment comment)
        {
            var filter = Builders<Comment>.Filter.Eq(c => c.id, comment.id);
            await _comments.ReplaceOneAsync(filter, comment);
            return comment;
        }

        public async Task<bool> DeleteAsync(int id)
        {
            var filter = Builders<Comment>.Filter.Eq(c => c.id, id);
            var result = await _comments.DeleteOneAsync(filter);
            return result.DeletedCount > 0;
        }

        // Пример генерации последовательного числового идентификатора

        private int GetNextSequenceValue(string sequenceName)
        {
            var countersCollection = _database.GetCollection<BsonDocument>("counters");
            var filter = Builders<BsonDocument>.Filter.Eq("_id", sequenceName);
            var update = Builders<BsonDocument>.Update.Inc("value", 1);
            var options = new FindOneAndUpdateOptions<BsonDocument>
            {
                ReturnDocument = ReturnDocument.After,
                IsUpsert = true
            };

            var updatedCounter = countersCollection.FindOneAndUpdate(filter, update, options);
            return updatedCounter["value"].AsInt32;
        }
    }

}
