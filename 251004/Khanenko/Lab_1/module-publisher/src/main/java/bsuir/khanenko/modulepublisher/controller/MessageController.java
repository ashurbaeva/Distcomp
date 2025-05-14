package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import bsuir.khanenko.modulepublisher.service.MessageService;
import bsuir.khanenko.modulepublisher.service.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseTo>> findAll() {
        return ResponseEntity.ok(messageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> findById(@PathVariable Long id) {
        Optional<MessageResponseTo> user = messageService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MessageResponseTo> create(@Valid @RequestBody MessageRequestTo userRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.create(userRequestTo));

    }

    @PutMapping()
    public ResponseEntity<MessageResponseTo> update(@Valid @RequestBody MessageUpdate userUpdate) {
        return ResponseEntity.ok(messageService.update(userUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        messageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}