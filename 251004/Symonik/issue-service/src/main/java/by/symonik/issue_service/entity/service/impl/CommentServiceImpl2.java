package by.symonik.issue_service.entity.service.impl;

import by.symonik.issue_service.client.CommentClient;
import by.symonik.issue_service.dto.comment.CommentRequestTo;
import by.symonik.issue_service.dto.comment.CommentResponseTo;
import by.symonik.issue_service.dto.comment.CommentUpdateRequestTo;
import by.symonik.issue_service.entity.service.CommentService;
import by.symonik.issue_service.kafka.KafkaProducer;
import by.symonik.issue_service.kafka.KafkaResponseCache;
import by.symonik.issue_service.mapper.CommentMapper;
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
        CompletableFuture<CommentResponseTo> future = KafkaResponseCache.createFuture(commentRequestTo.issueId());
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
