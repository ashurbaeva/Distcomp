using System.ComponentModel.DataAnnotations.Schema;

namespace LAB2.Domain;

public class Topic : Entity
{
    [Column("title")]
    public string Title { get; set; }

    [Column("content")]
    public string Content { get; set; }

    [Column("created")]
    public DateTime Created { get; set; } = DateTime.UtcNow;

    [Column("modified")]
    public DateTime Modified { get; set; } = DateTime.UtcNow;

    [Column("user_id")]
    public int UserId { get; set; }

    public User User { get; set; }
    public ICollection<Message> Messages { get; set; } = new List<Message>();
    public ICollection<Tag> Tags { get; set; } = new List<Tag>();
}