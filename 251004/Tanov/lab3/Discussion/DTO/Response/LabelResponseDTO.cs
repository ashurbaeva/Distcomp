﻿using Discussion.Models;

namespace Discussion.DTO.ResponseDTO;

public class LabelResponseDTO
{
    public long Id { get; set; }
    
    public long IssueId { get; set; }
    
    public string Content { get; set; }
}