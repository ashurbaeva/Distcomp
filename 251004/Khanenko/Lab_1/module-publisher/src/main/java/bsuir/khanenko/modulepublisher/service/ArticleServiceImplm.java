package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.article.ArticleRequestTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleResponseTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleUpdate;
import bsuir.khanenko.modulepublisher.entity.Article;
import bsuir.khanenko.modulepublisher.mapping.ArticleMapper;
import bsuir.khanenko.modulepublisher.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImplm implements ArticleService {

    private ArticleRepository articleRepository;
    private ArticleMapper articleMapper;

    @Autowired
    public ArticleServiceImplm(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public ArticleResponseTo create(ArticleRequestTo article) {
        return articleMapper.toResponse(articleRepository.create(articleMapper.toEntity(article)));
    }

    @Override
    public ArticleResponseTo update(ArticleUpdate updatedArticle) {
        Article article = articleRepository.findById(updatedArticle.getId())
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        if (updatedArticle.getUserId() != null) {
            article.setUserId(updatedArticle.getUserId());
        }
        if (updatedArticle.getTitle() != null) {
            article.setTitle(updatedArticle.getTitle());
        }
        if (updatedArticle.getContent() != null) {
            article.setContent(updatedArticle.getContent());
        }
        if (updatedArticle.getCreated() != null) {
            article.setCreated(updatedArticle.getCreated());
        }
        if (updatedArticle.getModified() != null) {
            article.setModified(updatedArticle.getModified());
        }
        if (updatedArticle.getLabels() != null) {
            article.setLabels(updatedArticle.getLabels());
        }

        return articleMapper.toResponse(articleRepository.update(article));
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);

    }

    @Override
    public List<ArticleResponseTo> findAll() {
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<ArticleResponseTo> findById(Long id) {
        return articleRepository.findById(id)
                .map(articleMapper::toResponse);
    }
}
