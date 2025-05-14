using System.ComponentModel.DataAnnotations.Schema;

namespace LAB2.Domain;

public class User : Entity
{
    [Column("login")]
    public string Login { get; set; }

    [Column("password")]
    public string Password { get; set; }

    [Column("firstname")]
    public string Firstname { get; set; }

    [Column("lastname")]
    public string Lastname { get; set; }

    public ICollection<Topic> Topics { get; set; } = new List<Topic>();
}