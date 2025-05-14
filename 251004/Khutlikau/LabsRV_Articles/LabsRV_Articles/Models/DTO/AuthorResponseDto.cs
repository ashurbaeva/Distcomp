using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{
    public class AuthorResponseDto
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("login")]
        public string Login { get; set; } = string.Empty;

        [JsonPropertyName("firstname")]
        public string FirstName { get; set; } = string.Empty;

        [JsonPropertyName("lastname")]
        public string LastName { get; set; } = string.Empty;

        // Если необходимо вернуть идентификаторы статей, связанные с данным автором.
        [JsonPropertyName("articleIds")]
        public IEnumerable<int> ArticleIds { get; set; } = new List<int>();
    }
}
