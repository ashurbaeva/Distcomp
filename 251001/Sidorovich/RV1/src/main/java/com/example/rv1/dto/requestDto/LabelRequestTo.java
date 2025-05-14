package com.example.rv1.dto.requestDto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class LabelRequestTo {
    private Long id;
    @Size(min = 2, max = 32)
    private String name;
}