namespace Publisher.Models.DTOs.Requests
{
    public class NewsSearchRequestDto
    {
        public string? StickerName { get; set; }
        public long? StickerId { get; set; }
        public string? EditorLogin { get; set; }
        public string? Title { get; set; }
        public string? Content { get; set; }
    }
}
