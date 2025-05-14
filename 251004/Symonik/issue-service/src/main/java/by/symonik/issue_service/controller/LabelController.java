package by.symonik.issue_service.controller;

import by.symonik.issue_service.dto.label.LabelRequestTo;
import by.symonik.issue_service.dto.label.LabelResponseTo;
import by.symonik.issue_service.dto.label.LabelUpdateRequestTo;
import by.symonik.issue_service.entity.service.LabelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/labels")
public class LabelController {
    private final LabelService labelService;

    @GetMapping
    public ResponseEntity<List<LabelResponseTo>> readAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(labelService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponseTo> readById(@PathVariable("id") @Valid @NotNull Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(labelService.readById(id));
    }

    @PostMapping
    public ResponseEntity<LabelResponseTo> create(@Valid @RequestBody LabelRequestTo labelRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(labelService.create(labelRequestTo));
    }

    @PutMapping
    public ResponseEntity<LabelResponseTo> update(@Valid @RequestBody LabelUpdateRequestTo labelUpdateRequestTo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(labelService.update(labelUpdateRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @Valid @NotNull Long id) {
        labelService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
