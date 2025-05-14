package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.article.ArticleRequestTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleResponseTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleUpdate;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    ArticleResponseTo create(ArticleRequestTo article);
    ArticleResponseTo update(ArticleUpdate updatedArticle);
    void deleteById(Long id);
    List<ArticleResponseTo> findAll();
    Optional<ArticleResponseTo> findById(Long id);
}
