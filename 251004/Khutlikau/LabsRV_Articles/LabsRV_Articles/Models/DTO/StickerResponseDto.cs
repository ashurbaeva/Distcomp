using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{
    public class StickerResponseDto
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("name")]
        public string Name { get; set; } = string.Empty;
    }
}
