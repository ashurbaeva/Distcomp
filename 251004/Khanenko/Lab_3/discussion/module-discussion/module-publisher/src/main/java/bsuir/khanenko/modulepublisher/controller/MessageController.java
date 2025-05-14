package bsuir.khanenko.modulepublisher.controller;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import bsuir.khanenko.modulepublisher.service.Impl.MessageService;
import jakarta.validation.Valid;
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

    // 1. Создание нового сообщения
    @PostMapping
    public ResponseEntity<MessageResponseTo> createMessage(@RequestBody MessageRequestTo requestTo) {
        MessageResponseTo responseTo = messageService.createMessage(requestTo);
        return ResponseEntity.ok(responseTo);
    }

    // 2. Получение всех сообщений
    @GetMapping
    public ResponseEntity<List<MessageResponseTo>> getAllMessages() {
        List<MessageResponseTo> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // 3. Получение сообщения по ключу
    @GetMapping("{id}")
    public ResponseEntity<MessageResponseTo> getMessageById(@PathVariable Long id) {

        MessageResponseTo responseTo = messageService.getMessageById(id);
        return ResponseEntity.ok(responseTo);
    }

    // 4. Обновление сообщения
    @PutMapping
    public ResponseEntity<MessageResponseTo> updateMessage(@Valid @RequestBody MessageUpdate messageUpdate) {

        MessageResponseTo responseTo = messageService.updateMessage(messageUpdate);
        return ResponseEntity.ok(responseTo);
    }

    // 5. Удаление сообщения
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {

        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}