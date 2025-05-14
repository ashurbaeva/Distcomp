using LabsRV_Articles.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace LabsRV_Articles.MyApp.Models.Domain
{
    [Index(nameof(articleId), nameof(stickerId), IsUnique = true)]
    public class ArticleSticker
    {
        [Required]
        public int articleId { get; set; }
        public Article article { get; set; }

        [Required]
        public int stickerId { get; set; }
        public Sticker sticker { get; set; }
    }
}
