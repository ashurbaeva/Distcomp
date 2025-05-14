package com.example.rv1.services;

import com.example.rv1.dto.requestDto.ArticleRequestTo;
import com.example.rv1.dto.requestDto.LabelRequestTo;
import com.example.rv1.dto.responseDto.ArticleResponseTo;
import com.example.rv1.entities.Article;
import com.example.rv1.entities.ArticleLabel;
import com.example.rv1.entities.Comment;
import com.example.rv1.entities.Label;
import com.example.rv1.mapper.ArticleMapper;
import com.example.rv1.repositories.ArticleLabelRepository;
import com.example.rv1.repositories.ArticleRepository;
import com.example.rv1.repositories.CreatorRepository;
import com.example.rv1.repositories.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;
    @Autowired
    private CreatorRepository creatorRepository;
   @Autowired
   private LabelRepository labelRepository;

    @Autowired
    private ArticleLabelRepository articleLabelRepository;

    public List<ArticleResponseTo> getAll() {
        // findAll() returns a List<Article>; convert it to a stream, map, and collect to List
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::from)
                .toList();
    }
    @Cacheable(value = "articles", key = "#id")
    public ArticleResponseTo get(long id) {
        // findById returns an Optional, which we either map to our response DTO or return null
        return articleRepository.findById(id)
                .map(articleMapper::from)
                .orElse(null);
    }

    @CachePut(value = "articles", key = "#input.id")
    public ArticleResponseTo create(ArticleRequestTo input) {
        if (!creatorRepository.findById(input.getCreatorId()).isPresent()) {
            throw new NoSuchElementException("Creator with ID " + input.getCreatorId() + " not found");
        }

        Article article = articleMapper.to(input);
        article.setCreated(ZonedDateTime.now());
        article.setModified(ZonedDateTime.now());
        Article savedArticle = articleRepository.save(article);

        List<String> labels =  input.getLabels();

        if (labels != null) {
            for (String labelText : labels) {
                Label label = new Label();
                label.setName(labelText);
                Long labelId = labelRepository.save(label).getId();

                ArticleLabel articleLabel = new ArticleLabel();
                articleLabel.setLabelId(labelId);
                articleLabel.setArticleId(savedArticle.getId());
                articleLabelRepository.save(articleLabel);
            }
        }
        input.setId(savedArticle.getId());
        return articleMapper.from(savedArticle);
    }
    @CachePut(value = "articles", key = "#input.id")
    public ArticleResponseTo update(ArticleRequestTo input) {
        // Map and update also means saving the entity:
//        Article article = articleMapper.to(input);
//        article.setModified(ZonedDateTime.now());
//        Article updatedArticle = articleRepository.save(article);
//        return articleMapper.from(updatedArticle);

        Article existingArticle = articleRepository.findById(input.getId()).orElse(null);
        assert existingArticle != null;
        existingArticle.setModified(ZonedDateTime.now());
        existingArticle.setCreatorId(input.getCreatorId());
        existingArticle.setContent(input.getContent());
        existingArticle.setTitle(input.getTitle());

        Article updatedArticle = articleRepository.save(existingArticle);
        input.setId(updatedArticle.getId());
        return articleMapper.from(updatedArticle);
    }
    @CacheEvict(value = "articles", key = "#id")
    public boolean delete(long id) {
        // Check if the article exists, then delete it
        if (articleRepository.existsById(id)) {
            Article article = articleRepository.findById(id).orElse(null);
            List<ArticleLabel> articleLabels = articleLabelRepository.findByArticleId(id);

            //System.out.println("\n\n\n\n\n\n\n\n\n\n\n"+articleLabels+"\n"+id+"\n\n\n\n\n\n\n\n\n\n\n");
            if (articleLabels != null) {
                for (ArticleLabel articleLabel : articleLabels) {
                    articleLabelRepository.delete(articleLabel);
                    labelRepository.deleteById(articleLabel.getLabelId());
                }
            }

            articleRepository.deleteById(id);


            return true;
        } else {
            return false;
        }
    }
}
