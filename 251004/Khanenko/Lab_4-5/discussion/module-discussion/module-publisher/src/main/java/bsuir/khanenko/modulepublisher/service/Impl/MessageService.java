package bsuir.khanenko.modulepublisher.service.Impl;

import bsuir.khanenko.modulepublisher.dto.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.InTopicDTO;
import bsuir.khanenko.modulepublisher.entity.Message;
import bsuir.khanenko.modulepublisher.exceptionHandler.UserNotFoundException;
import bsuir.khanenko.modulepublisher.mapping.MessageMapper;
import bsuir.khanenko.modulepublisher.repository.MessageRepository;
import bsuir.khanenko.modulepublisher.dto.OutTopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @KafkaListener(topics = "InTopic", groupId = "messages-group")
    @SendTo
    public org.springframework.messaging.Message<OutTopicDTO> handleMessageRequest(@Payload InTopicDTO request,
                                                                                   @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
                                                                                   @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId) {
        MessageRequestTo messageRequestTo = request.getMessageRequestTo();
        String method = request.getMethod();
        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                response = handleSave(messageRequestTo);
            } else if (method.equals("GET")) {
                response = messageRequestTo != null ? handleFindById(messageRequestTo.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(messageRequestTo);
            } else if (method.equals("DELETE")) {
                response = handleDelete(messageRequestTo.getId());
            } else {
                response = new OutTopicDTO("Unsupported method: " + method, "DECLINE");
            }
        } catch (Exception ex) {
            response = new OutTopicDTO("Error: " + ex.getMessage(), "DECLINE");
        }

        if (replyTopic != null && correlationId != null) {
            return MessageBuilder.withPayload(response)
                    .setHeader(KafkaHeaders.TOPIC, new String(replyTopic))
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
        } else {
            return null;
        }
    }

    private OutTopicDTO handleSave(MessageRequestTo dto) {
        Message message = messageMapper.toEntity(dto);
        String country = "Default";
        message.setCountry(country);
        Message savedMessage = messageRepository.save(message);
        return new OutTopicDTO(messageMapper.toResponse(savedMessage), "APPROVE");
    }

    public List<MessageResponseTo> findAll() {
        return messageMapper.toMessageResponseList(messageRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<MessageResponseTo> messageResponseDTOList = findAll();
        return new OutTopicDTO(messageResponseDTOList, "APPROVE");
    }

    public MessageResponseTo findById(Long id) {
        List<Message> allMessages = messageRepository.findAll();
        return allMessages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .map(messageMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }
    }

    private OutTopicDTO handleUpdate(MessageRequestTo dto) {
        Message currMessage = messageRepository.findAll().stream()
                .filter(message -> message.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(dto.getId()));

        currMessage.setContent(dto.getContent());
        Message updatedMessage = messageRepository.save(currMessage);
        return new OutTopicDTO(messageMapper.toResponse(updatedMessage), "APPROVE");
    }

    public MessageResponseTo update(MessageRequestTo dto) {
        Message currMessage = messageRepository.findAll().stream()
                .filter(message -> message.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(dto.getId()));

        currMessage.setContent(dto.getContent());
        Message updatedMessage = messageRepository.save(currMessage);
        return messageMapper.toResponse(updatedMessage);
    }

    private OutTopicDTO handleDelete(Long id) {
        Message currMessage = messageRepository.findAll().stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
        if (currMessage != null) {
            messageRepository.delete(currMessage);
        } else {
            throw new UserNotFoundException(id);
        }
        return new OutTopicDTO(messageMapper.toResponse(currMessage), "APPROVE");
    }

}