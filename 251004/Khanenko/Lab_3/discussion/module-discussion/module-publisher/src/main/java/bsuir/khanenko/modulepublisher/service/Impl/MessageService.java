package bsuir.khanenko.modulepublisher.service.Impl;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import bsuir.khanenko.modulepublisher.entity.Message;
import bsuir.khanenko.modulepublisher.exceptionHandler.UserNotFoundException;
import bsuir.khanenko.modulepublisher.mapping.MessageMapper;
import bsuir.khanenko.modulepublisher.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    // Создание нового сообщения
    public MessageResponseTo createMessage(MessageRequestTo requestTo) {
        Message message = messageMapper.toEntity(requestTo);
        message.setCountry("Default");
        message.setId((long) (Math.random() * 10000000));
        Message savedMessage = messageRepository.save(message);
        return messageMapper.toResponse(savedMessage);
    }

    // Получение всех сообщений
    public List<MessageResponseTo> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Получение сообщения по ключу
    public MessageResponseTo getMessageById(Long id) {
        List<Message> allMessages = messageRepository.findAll();
        return allMessages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .map(messageMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // Обновление сообщения
    public MessageResponseTo updateMessage(MessageUpdate messageUpdate) {
       Message currMessage = messageRepository.findAll().stream()
                .filter(message -> message.getId().equals(messageUpdate.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(messageUpdate.getId()));

        currMessage.setContent(messageUpdate.getContent());
        Message updatedMessage = messageRepository.save(currMessage);
        return messageMapper.toResponse(updatedMessage);
    }

    // Удаление сообщения
    public void deleteMessage(Long id) {
        Message currMessage = messageRepository.findAll().stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
        if (currMessage != null) {
            messageRepository.delete(currMessage);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}