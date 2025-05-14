namespace LAB2.Kafka;

public class MessageKafkaDto
{
    public int Id { get; set; }
    public string Content { get; set; }
    public int TopicId { get; set; }
    public string State { get; set; }
}