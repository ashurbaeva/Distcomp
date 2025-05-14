using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{
    public class ArticleRequestDto
    {
        [Required(ErrorMessage = "authorId is required.")]
        [JsonPropertyName("authorId")]
        public int AuthorId { get; set; }

        [Required(ErrorMessage = "title is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "title must be between 2 and 64 characters.")]
        [JsonPropertyName("title")]
        public string Title { get; set; } = string.Empty;

        [Required(ErrorMessage = "content is required.")]
        [StringLength(2048, MinimumLength = 4, ErrorMessage = "content must be between 4 and 2048 characters.")]
        [JsonPropertyName("content")]
        public string Content { get; set; } = string.Empty;

        // Дополнительное поле для связи с таблицей ArticleSticker.
        [JsonPropertyName("stickers")]
        public List<string> StickerNames { get; set; } = new List<string>();
    }
}
