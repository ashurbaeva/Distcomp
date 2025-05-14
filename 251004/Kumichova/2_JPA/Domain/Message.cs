using System.ComponentModel.DataAnnotations.Schema;

namespace LAB2.Domain;

public class Message : Entity
{
    [Column("content")]
    public string Content { get; set; }

    [Column("topic_id")]
    public int TopicId { get; set; }

    public Topic Topic { get; set; }
}