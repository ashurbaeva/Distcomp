package com.bsuir.dc.controller;

import com.bsuir.dc.dto.request.MessageRequestTo;
import com.bsuir.dc.dto.response.MessageResponseTo;
import com.bsuir.dc.service.MessageService;
import com.bsuir.dc.util.exception.ValidationException;
import com.bsuir.dc.util.validator.MessageValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageValidator messageValidator;

    @Autowired
    public MessageController(MessageService messageService, MessageValidator messageValidator) {
        this.messageService = messageService;
        this.messageValidator = messageValidator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseTo createMessage(@RequestBody @Valid MessageRequestTo messageRequestTo,
                                        BindingResult bindingResult) {
        validateRequest(messageRequestTo, bindingResult);
        return messageService.createMessage(messageRequestTo);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseTo getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseTo> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseTo updateNote(
            @RequestBody @Valid MessageRequestTo messageRequestTo) {
        return messageService.processMessageRequest("PUT", messageRequestTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id) {
        MessageRequestTo request = new MessageRequestTo();
        request.setId(id);
        messageService.processMessageRequest("DELETE", request);
    }

    private void validateRequest(MessageRequestTo request, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            messageValidator.validate(request, bindingResult);
        }
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}
