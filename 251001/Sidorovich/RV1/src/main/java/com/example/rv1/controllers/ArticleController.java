package com.example.rv1.controllers;

import com.example.rv1.dto.requestDto.ArticleRequestTo;
import com.example.rv1.dto.responseDto.ArticleResponseTo;
import com.example.rv1.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.Console;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Collection<ArticleResponseTo> getAll() { return articleService.getAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponseTo create(@RequestBody @Valid ArticleRequestTo input) {
        try{
            return articleService.create(input);
        }
        catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicate creator name");
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator with ID " + input.getCreatorId() + " not found");
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ArticleResponseTo update(@RequestBody @Valid ArticleRequestTo input) {
        try{ return articleService.update(input); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ArticleResponseTo get(@PathVariable long id) {
        try{ return articleService.get(id); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        boolean deleted = articleService.delete(id);
        if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}