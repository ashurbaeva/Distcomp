package by.ryabchikov.comment_service.kafka;

import by.ryabchikov.comment_service.dto.CommentResponseTo;
import by.ryabchikov.comment_service.mapper.CommentMapper;
import by.ryabchikov.comment_service.service.CommentService;
import by.ryabchikov.data.CommentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final KafkaProducer kafkaProducer;
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @SneakyThrows
    @KafkaListener(topics = "InTopic", groupId = "consumers")
    public void listen(CommentRequestEvent commentRequestEvent) {
        CommentResponseTo commentResponseTo = commentService.create(
                commentMapper.toCommentRequestTo(commentRequestEvent)
        );
        kafkaProducer.sendMessage(commentMapper.toCommentResponseEvent(commentResponseTo));
    }
}
