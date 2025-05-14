using Cassandra.Mapping.Attributes;

namespace Discussion.Models;

[Table("tbl_message")]
public class Note : BaseModel
{
    [ClusteringKey]
    public long IssueId { get; set; }
    
    public string Content { get; set; }
}