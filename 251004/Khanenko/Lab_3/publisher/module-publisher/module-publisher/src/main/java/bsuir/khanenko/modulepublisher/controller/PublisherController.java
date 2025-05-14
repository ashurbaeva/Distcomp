package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/messages")
public class PublisherController {

    private final DiscussionClient discussionClient;

    public PublisherController(DiscussionClient discussionClient) {
        this.discussionClient = discussionClient;
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseTo>> getAllMessages() {
        List<MessageResponseTo> messages = discussionClient.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // Создание нового сообщения
    @PostMapping
    public ResponseEntity<MessageResponseTo> createMessage(@RequestBody MessageRequestTo requestTo) {
        MessageResponseTo responseTo = discussionClient.createMessage(requestTo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseTo);
    }

    // Получение сообщения по ID
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> getMessageById(@PathVariable Long id) {
        try {
            MessageResponseTo responseTo = discussionClient.getMessageById(id);
            return ResponseEntity.ok(responseTo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Обновление сообщения
    @PutMapping
    public ResponseEntity<MessageResponseTo> updateMessage(
            @RequestBody MessageUpdate messageUpdate) {

        MessageResponseTo responseTo = discussionClient.updateMessage(messageUpdate);
        return ResponseEntity.ok(responseTo);
    }

    // Удаление сообщения
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        discussionClient.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
