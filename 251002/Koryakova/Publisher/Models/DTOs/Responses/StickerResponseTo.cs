using System.ComponentModel.DataAnnotations;

namespace Publisher.Models.DTOs.Responses
{
    public class StickerResponseTo
    {
        public long Id { get; set; }

        public string Name { get; set; } = string.Empty;
    }
}
