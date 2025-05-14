package com.example.rv1.dto.requestDto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class CommentRequestTo {
    private Long id;

    private Long articleId;

    @Size(min = 2, max = 2048)
    private String content;
}