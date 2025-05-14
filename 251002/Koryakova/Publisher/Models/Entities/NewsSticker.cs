using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models.Entities
{
    public class NewsSticker
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [ForeignKey(nameof(News))]
        public long NewsId { get; set; }

        [ForeignKey(nameof(Sticker))]
        public long StickerId { get; set; }
    }
}
