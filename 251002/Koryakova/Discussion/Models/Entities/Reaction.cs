using System.ComponentModel.DataAnnotations;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using Shared.Models;

namespace Discussion.Models.Entities
{
    public class Reaction
    {
        [BsonId]  // MongoDB primary key
        [BsonRepresentation(BsonType.ObjectId)] 
        public string? Id { get; set; }
        
        [BsonElement("country")]  // Stores as lowercase in MongoDB
        public string? Country { get; set; }  // ← Partition key

        [Required]
        [BsonElement("newsId")]
        public long NewsId { get; set; }

        [Required]
        [BsonElement("content")]
        [StringLength(2048, MinimumLength = 2)]
        public string Content { get; set; } = string.Empty;

        [BsonElement("state")]
        [BsonRepresentation(BsonType.String)]
        public ReactionState State { get; set; } = ReactionState.PENDING;
    }
}
