package com.example.rv1.mapper;

import com.example.rv1.dto.requestDto.ArticleRequestTo;
import com.example.rv1.dto.responseDto.ArticleLabelResponseTo;
import com.example.rv1.dto.responseDto.ArticleResponseTo;
import com.example.rv1.entities.Article;
import com.example.rv1.entities.ArticleLabel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleLabelMapper {
    ArticleLabelResponseTo from(ArticleLabel article);
    ArticleLabel to(ArticleLabelResponseTo article);
}
