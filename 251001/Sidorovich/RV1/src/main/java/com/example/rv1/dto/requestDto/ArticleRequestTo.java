package com.example.rv1.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data

public class ArticleRequestTo {
    private Long id;

    private Long creatorId;
    @Size(min = 2, max = 64)
    private String title;

    @Size(min = 4, max = 2048)
    private String content;


    private List<@NotBlank String> labels;
}