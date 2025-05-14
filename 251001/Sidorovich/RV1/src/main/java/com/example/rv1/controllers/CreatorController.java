package com.example.rv1.controllers;

import com.example.rv1.dto.requestDto.CreatorRequestTo;
import com.example.rv1.dto.responseDto.CreatorResponseTo;
import com.example.rv1.services.CreatorService;
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
@RequestMapping("/api/v1.0/creators")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

    @GetMapping
    public Collection<CreatorResponseTo> getAll() { return creatorService.getAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatorResponseTo create(@RequestBody @Valid CreatorRequestTo input) {

    try {
        return creatorService.create(input);
    }
    catch (DataIntegrityViolationException e){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicate creator name");
    }
}

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CreatorResponseTo update(@RequestBody @Valid CreatorRequestTo input) {
        try{ return creatorService.update(input); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public CreatorResponseTo get(@PathVariable long id) {
        try{ return creatorService.get(id); }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        boolean deleted = creatorService.delete(id);
        if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}