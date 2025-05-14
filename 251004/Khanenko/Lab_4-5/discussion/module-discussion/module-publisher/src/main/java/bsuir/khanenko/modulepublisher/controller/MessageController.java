package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.MessageResponseTo;
import bsuir.khanenko.modulepublisher.service.Impl.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping
    public ResponseEntity<List<MessageResponseTo>> getAllMessages() {
        List<MessageResponseTo> messages = messageService.findAll();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> getMessageById(@PathVariable Long id) {

        MessageResponseTo responseTo = messageService.findById(id);
        return ResponseEntity.ok(responseTo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseTo> update(@PathVariable(required = false) Long id, @RequestBody MessageRequestTo messageRequestTo) {
        if (id != null) {
            messageRequestTo.setId(id);
        }
        return ResponseEntity.ok(messageService.update(messageRequestTo));
    }
}