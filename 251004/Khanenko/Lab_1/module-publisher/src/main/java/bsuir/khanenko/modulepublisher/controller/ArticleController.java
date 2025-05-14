package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.article.ArticleRequestTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleResponseTo;
import bsuir.khanenko.modulepublisher.dto.article.ArticleUpdate;
import bsuir.khanenko.modulepublisher.service.ArticleService;
import bsuir.khanenko.modulepublisher.service.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseTo>> findAll() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseTo> findById(@PathVariable Long id) {
        Optional<ArticleResponseTo> user = articleService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArticleResponseTo> create(@Valid @RequestBody ArticleRequestTo userRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.create(userRequestTo));
    }

    @PutMapping()
    public ResponseEntity<ArticleResponseTo> update(@Valid @RequestBody ArticleUpdate userUpdate) {
        return ResponseEntity.ok(articleService.update(userUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}