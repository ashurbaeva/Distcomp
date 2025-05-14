package bsuir.khanenko.modulepublisher.mapping;

import bsuir.khanenko.modulepublisher.dto.article.ArticleRequestTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleResponseTo;
import bsuir.khanenko.modulepublisher.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    Article toEntity(ArticleRequestTo articleRequestTo);
    ArticleResponseTo toResponse(Article article);
}
