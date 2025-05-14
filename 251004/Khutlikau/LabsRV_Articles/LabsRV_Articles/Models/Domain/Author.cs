using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace LabsRV_Articles.Models.Domain
{
    [Index(nameof(id), IsUnique = true)]
    [Index(nameof(login), IsUnique = true)]
    public class Author : IEntity
    {
        public int id { get; set; }

        [Required]
        [StringLength(64, MinimumLength = 2)]
        public string login { get; set; }

        [Required]
        [StringLength(128, MinimumLength = 8)]
        public string password { get; set; }

        [Required]
        [StringLength(64, MinimumLength = 2)]
        public string firstname { get; set; }

        [Required]
        [StringLength(64, MinimumLength = 2)]
        public string lastname { get; set; }

        // Один автор – множество статей
        public ICollection<Article> articles { get; set; } = new List<Article>();
    }
}
