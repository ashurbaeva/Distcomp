namespace DistComp_1.Models;

public class Label : BaseModel
{
    public string Name { get; set; }

    public List<Issue> Issues { get; set; } = [];
}