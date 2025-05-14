﻿using Publisher.Models;

namespace Publisher.DTO.ResponseDTO;

public class LabelResponseDTO
{
    public long Id { get; set; }
    public string Name { get; set; }
    
    public List<Issue> Issues { get; set; } = [];
}