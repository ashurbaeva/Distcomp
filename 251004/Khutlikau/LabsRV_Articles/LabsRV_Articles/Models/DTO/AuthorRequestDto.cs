using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace LabsRV_Articles.Models.DTO
{
    public class AuthorRequestDto
    {
        [Required(ErrorMessage = "login is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "login must be between 2 and 64 characters.")]
        [JsonPropertyName("login")]
        public string Login { get; set; } = string.Empty;

        [Required(ErrorMessage = "password is required.")]
        [StringLength(128, MinimumLength = 8, ErrorMessage = "password must be between 8 and 128 characters.")]
        [JsonPropertyName("password")]
        public string Password { get; set; } = string.Empty;

        [Required(ErrorMessage = "firstname is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "firstname must be between 2 and 64 characters.")]
        [JsonPropertyName("firstname")]
        public string FirstName { get; set; } = string.Empty;

        [Required(ErrorMessage = "lastname is required.")]
        [StringLength(64, MinimumLength = 2, ErrorMessage = "lastname must be between 2 and 64 characters.")]
        [JsonPropertyName("lastname")]
        public string LastName { get; set; } = string.Empty;
    }
}
