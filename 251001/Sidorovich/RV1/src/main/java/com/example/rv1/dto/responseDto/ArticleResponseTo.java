package com.example.rv1.dto.responseDto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Data

public class ArticleResponseTo {
    private Long id;
    private Long creatorId;
    @Size(min = 2, max = 64)
    private String title;

    @Size(min = 4, max = 2048)
    private String content;

    private ZonedDateTime created;

    private ZonedDateTime modified;
}