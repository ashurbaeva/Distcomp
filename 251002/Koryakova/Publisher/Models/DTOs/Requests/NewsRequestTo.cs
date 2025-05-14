using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Models.DTOs.Requests
{
    public class NewsRequestTo
    {
        // no for post, yes for put
        [Range(1, long.MaxValue, ErrorMessage = "ID must be positive.")]
        public long? Id { get; set; }

        // manually validate it exists
        [Range(1, long.MaxValue, ErrorMessage = "FK must be positive.")]
        public long? EditorId { get; set; }

        // unique
        [Required(ErrorMessage = "Title is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "Title must consist of 2-64 characters.")]
        public string Title { get; set; } = string.Empty;

        [Required(ErrorMessage = "Content is required.")]
        [StringLength(2048, MinimumLength = 4, ErrorMessage = "Content must consist of 4-2048 characters.")]
        public string Content { get; set; } = string.Empty;
    }
}
