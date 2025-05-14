using Shared.Models;

namespace Shared.Models.DTOs.Responses
{
    public class ReactionResponseTo
    {
        public string? Id { get; set; } = string.Empty;

        public long NewsId { get; set; }

        public string Content { get; set; } = string.Empty;

        public string? Country { get; set; } = string.Empty;

        public ReactionState State { get; set; }
    }
}
