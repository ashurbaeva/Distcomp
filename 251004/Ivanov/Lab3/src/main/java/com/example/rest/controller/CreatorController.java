package com.example.rest.controller;

import com.example.rest.dto.creator.CreatorRequestTo;
import com.example.rest.dto.creator.CreatorResponseTo;
import com.example.rest.dto.creator.CreatorUpdate;
import com.example.rest.exceptionHandler.CreatorNotFoundException;
import com.example.rest.service.CreatorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1.0/authors")
public class CreatorController {

    private final CreatorService creatorService;

    public CreatorController(CreatorService creatorService) {
        this.creatorService = creatorService;
    }

    @GetMapping
    public ResponseEntity<List<CreatorResponseTo>> findAll() {
        return ResponseEntity.ok(creatorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatorResponseTo> findById(@PathVariable @Valid @NotNull Long id) {
        Optional<CreatorResponseTo> creator = creatorService.findById(id);
        return creator.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CreatorResponseTo> create(@Valid @RequestBody CreatorRequestTo creatorRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creatorService.create(creatorRequestTo));

    }

    @PutMapping()
    public ResponseEntity<CreatorResponseTo> update(@Valid @RequestBody CreatorUpdate creatorUpdate) {
        return ResponseEntity.ok(creatorService.update(creatorUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid @NotNull Long id) {
        creatorService.findById(id).orElseThrow(() -> new CreatorNotFoundException(id));
        creatorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
