package bsuir.khanenko.modulepublisher.service;

import bsuir.khanenko.modulepublisher.dto.message.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.message.MessageUpdate;
import bsuir.khanenko.modulepublisher.entity.Message;
import bsuir.khanenko.modulepublisher.mapping.MessageMapper;
import bsuir.khanenko.modulepublisher.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImplm implements MessageService {

    private MessageRepository messageRepository;
    private MessageMapper messageMapper;

    @Autowired
    public MessageServiceImplm(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageResponseTo create(MessageRequestTo message) {
        return messageMapper.toResponse(messageRepository.create(messageMapper.toEntity(message)));
    }

    @Override
    public MessageResponseTo update(MessageUpdate updatedMessage) {
        Message message = messageRepository.findById(updatedMessage.getId())
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        if (updatedMessage.getId() != null) {
            message.setId(updatedMessage.getId());
        }
        if (updatedMessage.getContent() != null) {
            message.setContent(updatedMessage.getContent());
        }
        if (updatedMessage.getArticleId() != null) {
            message.setArticleId(updatedMessage.getArticleId());
        }

        return messageMapper.toResponse(messageRepository.update(message));
    }

    @Override
    public void deleteById(Long id) {
        messageRepository.deleteById(id);

    }

    @Override
    public List<MessageResponseTo> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<MessageResponseTo> findById(Long id) {
        return messageRepository.findById(id)
                .map(messageMapper::toResponse);
    }
}
