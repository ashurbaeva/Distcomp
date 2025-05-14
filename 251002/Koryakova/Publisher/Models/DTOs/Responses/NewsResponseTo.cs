using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Models.DTOs.Responses
{
    public class NewsResponseTo
    {
        public long Id { get; set; }

        // EditorId is required at creation but becomes NULL if the editor is deleted
        public long? EditorId { get; set; }

        public string Title { get; set; } = string.Empty;

        public string Content { get; set; } = string.Empty;

        public DateTime Created { get; set; } = DateTime.UtcNow;

        public DateTime Modified { get; set; }
    }
}
