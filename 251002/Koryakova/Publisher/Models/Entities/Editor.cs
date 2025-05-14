using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Publisher.Models.Entities
{
    [Index(nameof(Login), IsUnique = true)]
    public class Editor
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long Id { get; set; }

        [Required(ErrorMessage = "Login is required.")]
        [MaxLength(64, ErrorMessage = "Login cannot exceed 64 characters.")]
        public string Login { get; set; } = string.Empty;

        [Required(ErrorMessage = "Password is required.")]
        [MaxLength(128, ErrorMessage = "Password cannot exceed 128 characters.")]
        public string Password { get; set; } = string.Empty;

        [Required(ErrorMessage = "First name is required.")]
        [MaxLength(64, ErrorMessage = "First name cannot exceed 64 characters.")]
        public string FirstName { get; set; } = string.Empty;

        [Required(ErrorMessage = "Last name is required.")]
        [MaxLength(64, ErrorMessage = "Last name cannot exceed 64 characters.")]
        public string LastName { get; set; } = string.Empty;
    }
}
