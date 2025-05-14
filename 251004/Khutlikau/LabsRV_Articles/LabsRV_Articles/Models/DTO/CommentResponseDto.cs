using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{

    public class CommentResponseDto
    {
        [JsonPropertyName("id")]
        public string Id { get; set; }  
        [JsonPropertyName("articleId")]
        public int ArticleId { get; set; }
        [JsonPropertyName("content")]
        public string Content { get; set; } = string.Empty;
    }
}

