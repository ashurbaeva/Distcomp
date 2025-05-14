package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface ArticleRepository extends CrudRepository<Article, Long> {
}
