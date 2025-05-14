using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LabsRV_Discussion.Models.DTO
{

    public class CommentResponseDto
    {
        [JsonPropertyName("id")]
        public string Id { get; set; }  // ObjectId как строка
        [JsonPropertyName("articleId")]
        public int ArticleId { get; set; }
        [JsonPropertyName("content")]
        public string Content { get; set; } = string.Empty;
    }
}

