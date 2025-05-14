using Discussion.Models;

namespace Discussion.DTO.ResponseDTO;

public class NoteResponseDTO
{
    public long Id { get; set; }
    
    public long IssueId { get; set; }
    
    public string Content { get; set; }
}