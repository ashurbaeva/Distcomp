namespace LAB1.Domain;

public class User : Entity
{
    public string Login { get; set; }
    public string Password { get; set; }
    public string Firstname { get; set; }
    public string Lastname { get; set; }
    public ICollection<Topic> Topics { get; set; } = new List<Topic>();
}