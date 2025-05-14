package com.example.rv1.dto.responseDto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.ZonedDateTime;

@Data

public class ArticleLabelResponseTo {
    private Long id;

    private Long articleId;
    private Long labelId;

}