namespace LAB1.Domain;

public class Topic : Entity
{
    public string Title { get; set; }
    public string Content { get; set; }
    public int UserId { get; set; }
    public User User { get; set; }
    public ICollection<Message> Messages { get; set; } = new List<Message>();
    public ICollection<Tag> Tags { get; set; } = new List<Tag>();
}