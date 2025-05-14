using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LabsRV_Discussion.Models.DTO
{
    public class CommentRequestDto
    {
        [Required(ErrorMessage = "articleId is required.")]
        [JsonPropertyName("articleId")]
        public int ArticleId { get; set; }

        [Required(ErrorMessage = "content is required.")]
        [StringLength(2048, MinimumLength = 2, ErrorMessage = "content must be between 2 and 2048 characters.")]
        [JsonPropertyName("content")]
        public string Content { get; set; } = string.Empty;
    }
}

