namespace LAB1.Domain;

public class Tag : Entity
{
    public string Name { get; set; }
    public ICollection<Topic> Topics { get; set; } = new List<Topic>();
}