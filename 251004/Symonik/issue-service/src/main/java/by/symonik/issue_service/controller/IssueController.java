package by.symonik.issue_service.controller;

import by.symonik.issue_service.dto.issue.IssueRequestTo;
import by.symonik.issue_service.dto.issue.IssueResponseTo;
import by.symonik.issue_service.dto.issue.IssueUpdateRequestTo;
import by.symonik.issue_service.entity.service.IssueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/issues")
public class IssueController {
    private final IssueService issueService;

    @GetMapping
    public ResponseEntity<List<IssueResponseTo>> readAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(issueService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseTo> readById(@PathVariable("id") @Valid @NotNull Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(issueService.readById(id));
    }

    @PostMapping
    public ResponseEntity<IssueResponseTo> create(@Valid @RequestBody IssueRequestTo issueRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(issueService.create(issueRequestTo));
    }

    @PutMapping
    public ResponseEntity<IssueResponseTo> update(@Valid @RequestBody IssueUpdateRequestTo issueUpdateRequestTo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(issueService.update(issueUpdateRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @Valid @NotNull Long id) {
        issueService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
