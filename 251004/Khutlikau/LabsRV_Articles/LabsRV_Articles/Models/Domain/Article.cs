using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.MyApp.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace LabsRV_Articles.Models.Domain
{
    [Index(nameof(id), IsUnique = true)]
    [Index(nameof(title), IsUnique = true)]
    public class Article : IEntity
    {
        public int id { get; set; }

        [Required]
        [Column ("author_id")]
        public int authorId { get; set; }
        public Author author { get; set; }

        [Required]
        [StringLength(64, MinimumLength = 2)]
        public string title { get; set; }

        [Required]
        [StringLength(2048, MinimumLength = 4)]
        public string content { get; set; }

        [Required]
        public DateTime created { get; set; }

        [Required]
        public DateTime modified { get; set; }

        // Одна статья – много комментариев
        //public ICollection<Comment> comments { get; set; } = new List<Comment>();

        // Многие-ко-многим через вспомогательную сущность
        public ICollection<ArticleSticker> articleStickers { get; set; } = new List<ArticleSticker>();
    }
}
