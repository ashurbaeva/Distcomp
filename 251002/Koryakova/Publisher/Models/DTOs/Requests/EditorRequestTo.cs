using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Publisher.Models.DTOs.Requests
{
    public class EditorRequestTo
    {
        // no for post, yes for put
        [Range(1, long.MaxValue, ErrorMessage = "ID must be positive.")]
        [JsonPropertyName("id")]
        public long? Id { get; set; }

        // unique
        [Required(ErrorMessage = "Login is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "Login must consist of 2-64 characters.")]
        public string Login { get; set; } = string.Empty;

        [Required(ErrorMessage = "Password is required.")]
        [StringLength(128, MinimumLength = 8, ErrorMessage = "Password must consist of 2-64 characters.")]
        public string Password { get; set; } = string.Empty;

        [Required(ErrorMessage = "First name is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "First name must consist of 2-64 characters.")]
        [JsonPropertyName("firstname")]
        public string FirstName { get; set; } = string.Empty;

        [JsonPropertyName("lastname")]
        [Required(ErrorMessage = "Last name is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "Last name must consist of 2-64 characters.")]
        public string LastName { get; set; } = string.Empty;
    }
}
