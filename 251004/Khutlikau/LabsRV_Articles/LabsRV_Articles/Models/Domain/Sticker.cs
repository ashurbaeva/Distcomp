using LabsRV_Articles.MyApp.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace LabsRV_Articles.Models.Domain
{
    [Index(nameof(id), IsUnique = true)]
    [Index(nameof(name), IsUnique = true)]
    public class Sticker : IEntity
    {
        public int id { get; set; }

        [Required]
        [StringLength(32, MinimumLength = 2)]
        public string name { get; set; }

        // Многие-ко-многим
        public ICollection<ArticleSticker> articleStickers { get; set; } = new List<ArticleSticker>();
    }
}
