using System.ComponentModel.DataAnnotations.Schema;

namespace LAB2.Domain;

public class Tag : Entity
{
    [Column("name")]
    public string Name { get; set; }
    public ICollection<Topic> Topics { get; set; } = new List<Topic>();
}