package com.example.discussion;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1.0/comments")
@RequiredArgsConstructor
public class CommentController {

    private final service commentService;

    @GetMapping
    public Collection<CommentResponse> getAll() { return commentService.getAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@RequestBody @Valid CommentRequest input) {


        try{
            var a = commentService.create(input);
        System.out.println(a.getArticleId());
        System.out.println(a.getContent());
        System.out.println(a.getId());
        return a;
        }
        catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse update(@RequestBody @Valid CommentRequest input) {
        try{ return commentService.update(input); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public CommentResponse get(@PathVariable Long id) {
        try{ var a =  commentService.get(id);
        if (a == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else return a;
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        boolean deleted = commentService.delete(id);
        if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}