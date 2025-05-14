package by.symonik.issue_service.kafka;

import by.symonik.issue_service.dto.comment.CommentResponseTo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaResponseCache {

    private static final ConcurrentHashMap<Long, CompletableFuture<CommentResponseTo>> futures = new ConcurrentHashMap<>();

    public static CompletableFuture<CommentResponseTo> createFuture(Long issueId) {
        CompletableFuture<CommentResponseTo> future = new CompletableFuture<>();
        futures.put(issueId, future);
        return future;
    }

    public static void completeFuture(Long issueId, CommentResponseTo response) {
        CompletableFuture<CommentResponseTo> future = futures.remove(issueId);
        if (future != null) {
            future.complete(response);
        }
    }
}
