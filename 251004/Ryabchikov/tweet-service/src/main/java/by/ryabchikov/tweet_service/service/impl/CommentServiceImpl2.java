package by.ryabchikov.tweet_service.service.impl;

import by.ryabchikov.tweet_service.client.CommentClient;
import by.ryabchikov.tweet_service.dto.comment.CommentRequestTo;
import by.ryabchikov.tweet_service.dto.comment.CommentResponseTo;
import by.ryabchikov.tweet_service.dto.comment.CommentUpdateRequestTo;
import by.ryabchikov.tweet_service.kafka.KafkaProducer;
import by.ryabchikov.tweet_service.kafka.KafkaResponseCache;
import by.ryabchikov.tweet_service.mapper.CommentMapper;
import by.ryabchikov.tweet_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl2 implements CommentService {
    private final CommentClient commentClient;
    private final KafkaProducer kafkaProducer;
    private final CommentMapper commentMapper;

    @SneakyThrows
    @Override
    @Transactional
    public CommentResponseTo create(CommentRequestTo commentRequestTo) {
        CompletableFuture<CommentResponseTo> future = KafkaResponseCache.createFuture(commentRequestTo.tweetId());
        kafkaProducer.createComment(commentMapper.toCommentRequestEvent(commentRequestTo));

        return future.get(1, TimeUnit.SECONDS);
    }

    @Override
    @Transactional
    public List<CommentResponseTo> readAll() {
        return commentClient.readAll();
    }

    @Override
    @Transactional
    @Cacheable(value = "comments", key = "#id")
    public CommentResponseTo readById(Long id) {
        return commentClient.readById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "comments", key = "#commentUpdateRequestTo.id()")
    public CommentResponseTo update(CommentUpdateRequestTo commentUpdateRequestTo) {
        return commentClient.update(commentUpdateRequestTo);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        commentClient.deleteById(id);
    }
}
