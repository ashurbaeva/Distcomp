package com.example.rv1.controllers;

import com.example.rv1.dto.requestDto.CommentRequestTo;
import com.example.rv1.dto.responseDto.CommentResponseTo;
import com.example.rv1.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public Collection<CommentResponseTo> getAll() { return commentService.getAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseTo create(@RequestBody @Valid CommentRequestTo input) {
        try{return commentService.create(input);}
         catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseTo update(@RequestBody @Valid CommentRequestTo input) {
        try{ return commentService.update(input); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public CommentResponseTo get(@PathVariable long id) {
        try{ return commentService.get(id); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        boolean deleted = commentService.delete(id);
        if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}