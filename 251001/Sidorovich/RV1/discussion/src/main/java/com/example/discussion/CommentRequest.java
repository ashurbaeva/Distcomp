package com.example.discussion;


import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data

public class CommentRequest {
    private Long id;

    private Long articleId;

    @Size(min = 2, max = 2048)
    private String content;
}