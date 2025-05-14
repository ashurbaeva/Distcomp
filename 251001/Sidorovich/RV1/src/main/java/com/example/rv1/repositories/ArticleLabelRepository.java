package com.example.rv1.repositories;

import com.example.rv1.entities.Article;
import com.example.rv1.entities.ArticleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleLabelRepository extends JpaRepository<ArticleLabel,Long> {
    List<ArticleLabel> findByArticleId(Long articleLabelId);
}