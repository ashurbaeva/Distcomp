package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.label.LabelRequestTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelResponseTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelUpdate;
import bsuir.khanenko.modulepublisher.service.LabelService;
import bsuir.khanenko.modulepublisher.service.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/labels")
public class LabelController {

    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public ResponseEntity<List<LabelResponseTo>> findAll() {
        return ResponseEntity.ok(labelService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponseTo> findById(@PathVariable Long id) {
        Optional<LabelResponseTo> user = labelService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LabelResponseTo> create(@Valid @RequestBody LabelRequestTo userRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(labelService.create(userRequestTo));

    }

    @PutMapping()
    public ResponseEntity<LabelResponseTo> update(@Valid @RequestBody LabelUpdate userUpdate) {
        return ResponseEntity.ok(labelService.update(userUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labelService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        labelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}