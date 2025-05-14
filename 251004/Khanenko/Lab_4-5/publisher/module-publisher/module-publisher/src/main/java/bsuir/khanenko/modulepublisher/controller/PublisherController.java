package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.MessageResponseTo;
import bsuir.khanenko.modulepublisher.service.Impl.DiscussionClient;
import bsuir.khanenko.modulepublisher.dto.MessageUpdate;
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

    @PostMapping
    public ResponseEntity<MessageResponseTo> createMessage(@RequestBody MessageRequestTo requestTo) {
        MessageResponseTo responseTo = discussionClient.createMessage(requestTo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseTo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> getMessageById(@PathVariable Long id) {
        try {
            MessageResponseTo responseTo = discussionClient.getMessageById(id);
            return ResponseEntity.ok(responseTo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<MessageResponseTo> updateMessage(
            @RequestBody MessageRequestTo requestTo) {

        MessageResponseTo responseTo = discussionClient.processMessageRequest("PUT", requestTo);
        return ResponseEntity.ok(responseTo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        MessageRequestTo request = new MessageRequestTo();
        request.setId(id);
        discussionClient.processMessageRequest("DELETE", request);
        return ResponseEntity.noContent().build();
    }
}
