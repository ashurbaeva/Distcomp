package by.symonik.comment_service.kafka;

import by.symonik.data.CommentResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, CommentResponseEvent> kafkaTemplate;

    public void sendMessage(CommentResponseEvent commentResponseEvent) {
        kafkaTemplate.send("OutTopic", String.valueOf(commentResponseEvent.issueId()), commentResponseEvent);
    }
}