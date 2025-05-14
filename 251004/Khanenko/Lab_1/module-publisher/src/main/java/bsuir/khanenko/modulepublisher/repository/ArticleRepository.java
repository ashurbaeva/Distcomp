package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Article;

import java.util.List;
import java.util.Optional;


public interface ArticleRepository {
    Article create(Article article);
    Article update(Article updatedArticle);
    void deleteById(Long id);
    List<Article> findAll();
    Optional<Article> findById(Long id);
}
