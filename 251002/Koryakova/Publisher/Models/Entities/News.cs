using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models.Entities
{
    [Index(nameof(Title), IsUnique = true)]
    public class News
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [ForeignKey(nameof(Editor))]
        public long? EditorId { get; set; }

        [Required]
        [MaxLength(64)]
        public string Title { get; set; } = string.Empty;

        [Required]
        [MaxLength(2048)]
        public string Content { get; set; } = string.Empty;

        [Required]
        public DateTime Created { get; set; } = DateTime.UtcNow;

        [Required]
        public DateTime Modified { get; set; }
    }
}