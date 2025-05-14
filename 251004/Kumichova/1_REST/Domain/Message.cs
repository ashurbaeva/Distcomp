namespace LAB1.Domain;

public class Message : Entity
{
    public string Content { get; set; }
    public int TopicId { get; set; }
    public Topic Topic { get; set; }
}