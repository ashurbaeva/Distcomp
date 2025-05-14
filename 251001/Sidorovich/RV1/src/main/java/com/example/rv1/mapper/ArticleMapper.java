package com.example.rv1.mapper;

import com.example.rv1.dto.requestDto.ArticleRequestTo;
import com.example.rv1.dto.responseDto.ArticleResponseTo;
import com.example.rv1.entities.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleResponseTo from(Article article);
    Article to(ArticleRequestTo article);
}
