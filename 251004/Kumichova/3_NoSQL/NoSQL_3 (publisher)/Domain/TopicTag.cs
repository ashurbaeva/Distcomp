namespace LAB2.Domain;

public class TopicTag
{
    public int TopicId { get; set; }
    public int TagId { get; set; }
    public Topic Topic { get; set; }
    public Tag Tag { get; set; }
}