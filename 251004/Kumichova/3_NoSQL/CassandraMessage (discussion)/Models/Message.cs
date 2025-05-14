using Cassandra.Mapping.Attributes;

namespace CassandraMessages.Models;

[Table("tbl_message")]
public class Message
{
    [Column("id")]
    [PartitionKey]
    public Guid Id { get; set; }

    [Column("content")]
    public string Content { get; set; }

    [Column("topic_id")]
    public int TopicId { get; set; }

    [Column("created_at")]
    public DateTime CreatedAt { get; set; }

    [Column("country")]
    public string Country { get; set; } = "default"; // Для распределения по нодам
}