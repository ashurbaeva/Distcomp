package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    MessageResponseTo create(MessageRequestTo message);

    MessageResponseTo update(MessageUpdate updatedMessage);

    void deleteById(Long id);

    List<MessageResponseTo> findAll();

    Optional<MessageResponseTo> findById(Long id);
}
