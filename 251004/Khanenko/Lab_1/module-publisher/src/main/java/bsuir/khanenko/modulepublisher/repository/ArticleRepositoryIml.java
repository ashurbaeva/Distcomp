package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Article;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ArticleRepositoryIml implements ArticleRepository{

    private final Map<Long, Article> articles = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Article create(Article article) {
        article.setId(nextId++);
        articles.put(article.getId(), article);
        return article;
    }

    @Override
    public Article update(Article updatedArticle) {
        if (!articles.containsKey(updatedArticle.getId())) {
            throw new IllegalArgumentException("Article with ID " + updatedArticle.getId() + " not found");
        }
        articles.put(updatedArticle.getId(), updatedArticle);
        return updatedArticle;
    }

    @Override
    public void deleteById(Long id) {
        articles.remove(id);

    }

    @Override
    public List<Article> findAll() {
        return articles.values().stream().toList();
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articles.get(id));
    }
}
