using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{
    public class ArticleResponseDto
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("authorId")]
        public int AuthorId { get; set; }

        [JsonPropertyName("title")]
        public string Title { get; set; } = string.Empty;

        [JsonPropertyName("content")]
        public string Content { get; set; } = string.Empty;

        [JsonPropertyName("created")]
        public DateTime Created { get; set; }

        [JsonPropertyName("modified")]
        public DateTime Modified { get; set; }

        [JsonPropertyName("stickers")]
        public List<string> StickerNames { get; set; } = new List<string>();
    }
}
