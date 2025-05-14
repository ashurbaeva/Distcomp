package com.bsuir.dc.service;

import com.bsuir.dc.dto.kafka.InTopicDTO;
import com.bsuir.dc.dto.kafka.OutTopicDTO;
import com.bsuir.dc.dto.request.MessageRequestTo;
import com.bsuir.dc.dto.response.MessageResponseTo;
import com.bsuir.dc.util.exception.EntityNotFoundException;
import com.bsuir.dc.util.mapper.MessageMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.KafkaReplyTimeoutException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessageService {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final MessageMapper messageMapper;
    private static final String IN_TOPIC = "InTopic";
    private static final String OUT_TOPIC = "OutTopic";

    @Autowired
    public MessageService(
            ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate,
            KafkaTemplate<String, InTopicDTO> kafkaTemplate,
            MessageMapper messageMapper
    ) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.messageMapper = messageMapper;
    }

    public MessageResponseTo createMessage(MessageRequestTo requestDTO) {
        Long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        requestDTO.setId(generatedId);

        InTopicDTO inDto = new InTopicDTO(
                "POST",
                requestDTO,
                "PENDING"
        );

        kafkaTemplate.send(IN_TOPIC, generatedId.toString(), inDto);

        return messageMapper.toMessageResponse(requestDTO);
    }

    public List<MessageResponseTo> getAllMessages() {
        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        return response.getMessageResponsesListDTO();
    }

    @Cacheable(value = "messages", key = "#id")
    public MessageResponseTo getMessageById(Long id) {
        InTopicDTO request = new InTopicDTO(
                "GET",
                new MessageRequestTo(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()){
            throw new EntityNotFoundException(response.getError());
        }
        return response.getMessageResponseDTO();
    }

    @CacheEvict(value = "messages", key = "#messageRequestDTO.id")
    public MessageResponseTo processMessageRequest(String httpMethod, MessageRequestTo messageRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                messageRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, messageRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")){
            throw new EntityNotFoundException(response.getError());
        }
        return response.getMessageResponseDTO();

    }

    private OutTopicDTO sendAndReceiveInternal(InTopicDTO request, String correlationId) {
        ProducerRecord<String, InTopicDTO> record = new ProducerRecord<>(IN_TOPIC, correlationId, request);
        RequestReplyFuture<String, InTopicDTO, OutTopicDTO> future = replyingKafkaTemplate.sendAndReceive(record);
        try {
            ConsumerRecord<String, OutTopicDTO> response = future.get(10, TimeUnit.SECONDS);
            return response.value();
        } catch (TimeoutException e) {
            throw new KafkaReplyTimeoutException("Timeout waiting for message data");
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaException("Error processing Kafka request", e);
        }
    }
}
