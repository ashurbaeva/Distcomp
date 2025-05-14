package com.bsuir.discussion.services;

import com.bsuir.discussion.dto.kafka.InTopicDTO;
import com.bsuir.discussion.dto.kafka.OutTopicDTO;
import com.bsuir.discussion.dto.requests.MessageRequestDTO;
import com.bsuir.discussion.dto.responses.MessageResponseDTO;
import com.bsuir.discussion.models.Message;
import com.bsuir.discussion.repositories.MessagesRepository;
import com.bsuir.discussion.utils.mappers.MessagesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class MessagesService {
    private final MessagesRepository messagesRepository;
    private final MessagesMapper messagesMapper;

    @Value("${message.country}")
    private String country;

    @Autowired
    public MessagesService(MessagesRepository messagesRepository, MessagesMapper messagesMapper) {
        this.messagesRepository = messagesRepository;
        this.messagesMapper = messagesMapper;
    }

    @KafkaListener(topics = "InTopic", groupId = "messages-group")
    @SendTo
    public org.springframework.messaging.Message<OutTopicDTO> handleMessageRequest(
            @Payload InTopicDTO request,
            @Header(name = KafkaHeaders.REPLY_TOPIC, required = false) byte[] replyTopic,
            @Header(name = KafkaHeaders.CORRELATION_ID, required = false) byte[] correlationId
    ) {
        MessageRequestDTO messageRequestDTO = request.getMessageRequestDTO();
        String method = request.getMethod();
        OutTopicDTO response;

        try {
            if (method.equals("POST")) {
                handleSave(messageRequestDTO);
                return null;
            } else if (method.equals("GET")) {
                response = messageRequestDTO != null ? handleFindById(messageRequestDTO.getId()) : handleFindAll();
            } else if (method.equals("PUT")) {
                response = handleUpdate(messageRequestDTO);
            } else if (method.equals("DELETE")) {
                response = handleDelete(messageRequestDTO.getId());
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

    public MessageResponseDTO save(MessageRequestDTO messageRequestDTO) {
        Message message = messagesMapper.toMessage(messageRequestDTO);
        message.getKey().setId(Math.abs(UUID.randomUUID().getMostSignificantBits()));
        return messagesMapper.toMessageResponse(messagesRepository.save(message));
    }

    private OutTopicDTO handleSave(MessageRequestDTO dto) {
        Message message = messagesMapper.toMessage(dto);
        message.getKey().setId(dto.getId());
        message.getKey().setCountry(country);
        messagesRepository.save(message);
        return new OutTopicDTO(messagesMapper.toMessageResponse(message), "APPROVE");
    }

    public List<MessageResponseDTO> findAll() {
        return messagesMapper.toMessageResponseList(messagesRepository.findAll());
    }

    private OutTopicDTO handleFindAll() {
        List<MessageResponseDTO> messageResponseDTOList = findAll();
        return new OutTopicDTO(messageResponseDTOList, "APPROVE");
    }

    public MessageResponseDTO findById(Long id) {
        return messagesMapper.toMessageResponse(
                messagesRepository.findByCountryAndId(country, id)
                        .orElseThrow(() -> new RuntimeException(String.valueOf(id)))
        );
    }

    private OutTopicDTO handleFindById(Long id) {
        try {
            return new OutTopicDTO(findById(id), "APPROVE");
        } catch (RuntimeException ex) {
            return new OutTopicDTO(ex.getMessage(), "DECLINE");
        }
    }

    public void deleteById(long id) {
        messagesRepository.deleteByCountryAndId(country, id);
    }

    private OutTopicDTO handleUpdate(MessageRequestDTO dto) {
        Message message = messagesMapper.toMessage(dto);
        message.getKey().setId(dto.getId());
        message.getKey().setCountry(country);
        messagesRepository.save(message);
        return new OutTopicDTO(messagesMapper.toMessageResponse(message), "APPROVE");
    }

    public MessageResponseDTO update(MessageRequestDTO messageRequestDTO) {
        Message message = messagesMapper.toMessage(messageRequestDTO);
        message.getKey().setId(messageRequestDTO.getId());
        message.getKey().setCountry(country);
        return messagesMapper.toMessageResponse(messagesRepository.save(message));
    }

    private OutTopicDTO handleDelete(Long id) {
        Optional<Message> optionalMessage = messagesRepository.findByCountryAndId(country, id);

        if (optionalMessage.isEmpty()) {
            return new OutTopicDTO("Message not found", "DECLINE");
        }

        Message message = optionalMessage.get();
        messagesRepository.delete(message);
        return new OutTopicDTO(messagesMapper.toMessageResponse(message), "APPROVE");
    }
}
