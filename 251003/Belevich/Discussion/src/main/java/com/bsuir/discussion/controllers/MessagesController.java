package com.bsuir.discussion.controllers;

import com.bsuir.discussion.dto.requests.MessageRequestDTO;
import com.bsuir.discussion.dto.responses.MessageResponseDTO;
import com.bsuir.discussion.services.MessagesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private final MessagesService messagesService;

    @Autowired
    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseDTO> getAllMessages() {
        return messagesService.findAll();
    }

    @GetMapping("/{id}")
    public MessageResponseDTO getMessageById(@PathVariable Long id) {
        try {
            return messagesService.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO updateMessage(@RequestBody @Valid MessageRequestDTO messageRequestDTO) {
        return messagesService.update(messageRequestDTO);
    }
}
