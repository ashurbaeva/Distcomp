package bsuir.khanenko.modulepublisher.service.Impl;

import bsuir.khanenko.modulepublisher.mapping.MessageMapper;
import bsuir.khanenko.modulepublisher.dto.InTopicDTO;
import bsuir.khanenko.modulepublisher.dto.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.MessageResponseTo;
import bsuir.khanenko.modulepublisher.dto.OutTopicDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Service
public class DiscussionClient {
    private final ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate;
    private final KafkaTemplate<String, InTopicDTO> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MessageMapper messageMapper;
    private static final String MESSAGE_CACHE_PREFIX = "message:";
    private static final String IN_TOPIC = "InTopic";
    private static final String OUT_TOPIC = "OutTopic";

    @Autowired
    public DiscussionClient(ReplyingKafkaTemplate<String, InTopicDTO, OutTopicDTO> replyingKafkaTemplate,
                            KafkaTemplate<String, InTopicDTO> kafkaTemplate,
                            MessageMapper messageMapper, RedisTemplate<String, Object> redisTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.messageMapper = messageMapper;
        this.redisTemplate = redisTemplate;
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

        String allMessagesCacheKey = MESSAGE_CACHE_PREFIX + "all";
        redisTemplate.delete(allMessagesCacheKey);

        return messageMapper.toMessageResponse(requestDTO);
    }

    public List<MessageResponseTo> getAllMessages() {
        String cacheKey = MESSAGE_CACHE_PREFIX + "all";
        List<MessageResponseTo> cachedMessages = (List<MessageResponseTo>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            System.out.println("All messages found in Redis: " + cachedMessages);
            return cachedMessages;
        }

        InTopicDTO request = new InTopicDTO(
                "GET",
                null,
                "PENDING"
        );
        String id = UUID.randomUUID().toString();
        OutTopicDTO response = sendAndReceiveInternal(request, id);
        redisTemplate.opsForValue().set(cacheKey, response.getMessageResponseTos());
        return response.getMessageResponseTos();
    }

    public MessageResponseTo getMessageById(Long id) {
        String cacheKey = MESSAGE_CACHE_PREFIX + id;

        MessageResponseTo cachedMessage = (MessageResponseTo) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMessage != null) {
            System.out.println("Message found in Redis: " + cachedMessage);
            return cachedMessage;
        }

        InTopicDTO request = new InTopicDTO(
                "GET",
                new MessageRequestTo(id),
                "PENDING"
        );
        OutTopicDTO response = sendAndReceiveInternal(request, id.toString());
        if (response.getError() != null && !response.getError().isEmpty()){
            throw new RuntimeException(response.getError());
        }
        redisTemplate.opsForValue().set(cacheKey, response.getMessageResponseTo());

        return response.getMessageResponseTo();
    }


    public MessageResponseTo processMessageRequest(String httpMethod, MessageRequestTo commentRequestDTO) {
        InTopicDTO request = new InTopicDTO(
                httpMethod,
                commentRequestDTO,
                "PENDING"
        );

        OutTopicDTO response = sendAndReceiveInternal(request, commentRequestDTO.getId().toString());
        if (response.getError() != null && !response.getError().contains("Not found")){
            throw new RuntimeException(response.getError());
        }

        String cacheKey = MESSAGE_CACHE_PREFIX + commentRequestDTO.getId();
        redisTemplate.delete(cacheKey);

        String allMessagesCacheKey = MESSAGE_CACHE_PREFIX + "all";
        redisTemplate.delete(allMessagesCacheKey);

        return response.getMessageResponseTo();

    }


    private OutTopicDTO sendAndReceiveInternal(InTopicDTO request, String correlationId) {
        ProducerRecord<String, InTopicDTO> record = new ProducerRecord<>(IN_TOPIC, correlationId, request);
        RequestReplyFuture<String, InTopicDTO, OutTopicDTO> future = replyingKafkaTemplate.sendAndReceive(record);
        try {
            ConsumerRecord<String, OutTopicDTO> response = future.get();
            return response.value();
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaException("Error processing Kafka request", e);
        }
    }

}
