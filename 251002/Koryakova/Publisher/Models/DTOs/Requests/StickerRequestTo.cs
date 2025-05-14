using System.ComponentModel.DataAnnotations;

namespace Publisher.Models.DTOs.Requests
{
    public class StickerRequestTo
    {
        // yes for put, no for post
        [Range(1, long.MaxValue, ErrorMessage = "ID must be positive.")]
        public long? Id { get; set; }

        // unique
        [Required(ErrorMessage = "Name is required.")]
        [StringLength(32, MinimumLength = 2, ErrorMessage = "Name must consist of 2-32 characters.")]
        public string Name { get; set; } = string.Empty;
    }
}