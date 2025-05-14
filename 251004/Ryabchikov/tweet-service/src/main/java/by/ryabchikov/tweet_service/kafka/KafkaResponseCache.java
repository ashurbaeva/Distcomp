package by.ryabchikov.tweet_service.kafka;

import by.ryabchikov.tweet_service.dto.comment.CommentResponseTo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaResponseCache {

    private static final ConcurrentHashMap<Long, CompletableFuture<CommentResponseTo>> futures = new ConcurrentHashMap<>();

    public static CompletableFuture<CommentResponseTo> createFuture(Long tweetId) {
        CompletableFuture<CommentResponseTo> future = new CompletableFuture<>();
        futures.put(tweetId, future);
        return future;
    }

    public static void completeFuture(Long tweetId, CommentResponseTo response) {
        CompletableFuture<CommentResponseTo> future = futures.remove(tweetId);
        if (future != null) {
            future.complete(response);
        }
    }
}
