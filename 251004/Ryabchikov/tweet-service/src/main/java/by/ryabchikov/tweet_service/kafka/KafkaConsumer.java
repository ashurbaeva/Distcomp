package by.ryabchikov.tweet_service.kafka;

import by.ryabchikov.data.CommentResponseEvent;
import by.ryabchikov.tweet_service.dto.comment.CommentResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "OutTopic", groupId = "consumers")
    public void listen(CommentResponseEvent commentResponseEvent) {
        CommentResponseTo commentResponseTo =
                new CommentResponseTo(commentResponseEvent.id(), commentResponseEvent.tweetId(), commentResponseEvent.content());
        KafkaResponseCache.completeFuture(commentResponseEvent.tweetId(), commentResponseTo);
    }
}
