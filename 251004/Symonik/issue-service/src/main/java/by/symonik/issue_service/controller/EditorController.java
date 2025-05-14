package by.symonik.issue_service.controller;

import by.symonik.issue_service.dto.editor.EditorRequestTo;
import by.symonik.issue_service.dto.editor.EditorResponseTo;
import by.symonik.issue_service.dto.editor.EditorUpdateRequestTo;
import by.symonik.issue_service.entity.service.EditorService;
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
@RequestMapping("/api/v1.0/editors")
public class EditorController {
    private final EditorService editorService;

    @GetMapping
    public ResponseEntity<List<EditorResponseTo>> readAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(editorService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorResponseTo> readById(@PathVariable("id") @Valid @NotNull Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(editorService.readById(id));
    }

    @PostMapping
    public ResponseEntity<EditorResponseTo> create(@Valid @RequestBody EditorRequestTo editorRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(editorService.create(editorRequestTo));
    }

    @PutMapping
    public ResponseEntity<EditorResponseTo> update(@Valid @RequestBody EditorUpdateRequestTo editorUpdateRequestTo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(editorService.update(editorUpdateRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @Valid @NotNull Long id) {
        editorService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
