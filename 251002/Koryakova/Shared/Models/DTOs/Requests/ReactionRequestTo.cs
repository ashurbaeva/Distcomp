using Shared.Models;
using System.ComponentModel.DataAnnotations;

namespace Shared.Models.DTOs.Requests
{
    public class ReactionRequestTo
    {
        // no post, yea put
        [RegularExpression(@"^([0-9a-fA-F]{24}|\d+)$", ErrorMessage = "ID must be ObjectId or numeric.")]
        public string? Id { get; set; }

        // manual validation it exists
        [Required(ErrorMessage = "NewsId is required.")]
        [Range(1, long.MaxValue, ErrorMessage = "NewsId must be positive.")]
        public long NewsId { get; set; }

        [Required(ErrorMessage = "Content is required.")]
        [StringLength(2048, MinimumLength = 2, ErrorMessage = "Reaction content must consist of 2-2048 characters.")]
        public string Content { get; set; } = string.Empty;

        public string? Country { get; set; } = string.Empty;

        public ReactionState State { get; set; } = ReactionState.PENDING;
    }
}