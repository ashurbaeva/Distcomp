package com.example.rv1.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data

public class ArticleLabelRequestTo {
    private Long id;

    private Long articleId;
    private Long labelId;


}