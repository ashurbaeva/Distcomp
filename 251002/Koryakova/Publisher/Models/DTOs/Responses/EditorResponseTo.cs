using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Publisher.Models.DTOs.Responses
{
    public class EditorResponseTo
    {
        [JsonPropertyName("id")]
        public long Id { get; set; }

        public string Login { get; set; } = string.Empty;

        [JsonPropertyName("firstname")]
        public string FirstName { get; set; } = string.Empty;

        [JsonPropertyName("lastname")]
        public string LastName { get; set; } = string.Empty;
    }
}
