package by.symonik.issue_service.kafka;

import by.symonik.data.CommentResponseEvent;
import by.symonik.issue_service.dto.comment.CommentResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "OutTopic", groupId = "consumers")
    public void listen(CommentResponseEvent commentResponseEvent) {
        CommentResponseTo commentResponseTo =
                new CommentResponseTo(commentResponseEvent.id(), commentResponseEvent.issueId(), commentResponseEvent.content());
        KafkaResponseCache.completeFuture(commentResponseEvent.issueId(), commentResponseTo);
    }
}
