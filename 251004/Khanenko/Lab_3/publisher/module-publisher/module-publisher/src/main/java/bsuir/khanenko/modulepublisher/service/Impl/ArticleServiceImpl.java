package bsuir.khanenko.modulepublisher.service.Impl;

import bsuir.khanenko.modulepublisher.dto.article.ArticleRequestTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleResponseTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleUpdate;
import bsuir.khanenko.modulepublisher.entity.Article;
import bsuir.khanenko.modulepublisher.entity.Label;
import bsuir.khanenko.modulepublisher.entity.User;
import bsuir.khanenko.modulepublisher.exceptionHandler.UserNotFoundException;
import bsuir.khanenko.modulepublisher.mapping.ArticleMapper;
import bsuir.khanenko.modulepublisher.repository.ArticleRepository;
import bsuir.khanenko.modulepublisher.repository.LabelRepository;
import bsuir.khanenko.modulepublisher.repository.UserRepository;
import bsuir.khanenko.modulepublisher.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private ArticleMapper articleMapper;
    private LabelRepository labelRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper, UserRepository userRepository, LabelRepository labelRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public ArticleResponseTo create(ArticleRequestTo article) {
        User user = userRepository.findById(article.getUserId())
                .orElseThrow(() -> new UserNotFoundException(article.getUserId()));

        List<Label> labels = article.getLabels().stream()
                .map(labelName -> labelRepository.findByName(labelName)
                        .orElseGet(() -> {
                            Label newLabel = new Label();
                            newLabel.setName(labelName);
                            return labelRepository.save(newLabel);
                        }))
                .toList();

        Article entity = articleMapper.toEntity(article);
        entity.setUser(user);
        entity.setLabels(labels);

        return articleMapper.toResponse(articleRepository.save(entity));
    }

    @Override
    public ArticleResponseTo update(ArticleUpdate updatedArticle) {
        Article article = articleRepository.findById(updatedArticle.getId())
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));


        if (updatedArticle.getUserId() != null) {
            User user = userRepository.findById(updatedArticle.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User to update not found"));
            article.setUser(user);
        }
        if (updatedArticle.getTitle() != null) {
            article.setTitle(updatedArticle.getTitle());
        }
        if (updatedArticle.getContent() != null) {
            article.setContent(updatedArticle.getContent());
        }
        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);

    }

    @Override
    public List<ArticleResponseTo> findAll() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), false)
                .map(articleMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<ArticleResponseTo> findById(Long id) {
        return articleRepository.findById(id)
                .map(articleMapper::toResponse);
    }
}
