package by.ryabchikov.tweet_service.kafka;

import by.ryabchikov.data.CommentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, CommentRequestEvent> kafkaTemplate;

    @SneakyThrows
    public void createComment(CommentRequestEvent commentRequestEvent) {
        kafkaTemplate.send("InTopic", String.valueOf(commentRequestEvent.tweetId()), commentRequestEvent);
    }
}
