using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{
    public class StickerRequestDto
    {
        [Required(ErrorMessage = "name is required.")]
        [StringLength(32, MinimumLength = 2, ErrorMessage = "name must be between 2 and 32 characters.")]
        [JsonPropertyName("name")]
        public string Name { get; set; } = string.Empty;
    }
}
