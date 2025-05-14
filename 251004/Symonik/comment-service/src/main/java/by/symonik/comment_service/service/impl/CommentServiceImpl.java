package by.symonik.comment_service.service.impl;

import by.symonik.comment_service.client.IssueClient;
import by.symonik.comment_service.dto.CommentRequestTo;
import by.symonik.comment_service.dto.CommentResponseTo;
import by.symonik.comment_service.dto.CommentUpdateRequestTo;
import by.symonik.comment_service.entity.Comment;
import by.symonik.comment_service.exception.CommentNotCreatedException;
import by.symonik.comment_service.exception.CommentNotFoundException;
import by.symonik.comment_service.mapper.CommentMapper;
import by.symonik.comment_service.repository.CommentRepository;
import by.symonik.comment_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final IssueClient issueClient;

    @Override
    public CommentResponseTo create(CommentRequestTo commentRequestTo) {
        if(issueClient.readById(commentRequestTo.issueId()) == null) {
            throw CommentNotCreatedException.byInvalidIssueId(commentRequestTo.issueId());
        }

        Comment comment = commentMapper.toComment(commentRequestTo);
        comment.setCountry("Default");
        comment.setId((long) (Math.random() * 10000000));

        CommentResponseTo commentResponseTo = commentMapper.toCommentResponseTo(
                commentRepository.save(comment)
        );

        return commentResponseTo;
    }

    @Override
    public List<CommentResponseTo> readAll() {
        ArrayList<CommentResponseTo> commentResponseTos = new ArrayList<>();
        commentRepository.findAll().forEach(comment -> {
            CommentResponseTo commentResponseTo = commentMapper.toCommentResponseTo(comment);
            commentResponseTos.add(commentResponseTo);
        });
        return commentResponseTos;
    }

    @Override
    public CommentResponseTo readById(Long id) {
        return commentMapper.toCommentResponseTo(
                commentRepository.findById(id).orElseThrow(() -> CommentNotFoundException.byId(id))
        );
    }

    @Override
    public CommentResponseTo update(CommentUpdateRequestTo commentUpdateRequestTo) {
        long commentId = commentUpdateRequestTo.id();
        Comment comment =
                commentRepository.findById(commentId).orElseThrow(() -> CommentNotFoundException.byId(commentId));

        if(issueClient.readById(commentUpdateRequestTo.issueId()) == null) {
            throw CommentNotCreatedException.byInvalidIssueId(commentUpdateRequestTo.issueId());
        }

        comment.setIssueId(commentUpdateRequestTo.issueId());
        comment.setContent(commentUpdateRequestTo.content());
        commentRepository.save(comment);

        return commentMapper.toCommentResponseTo(comment);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> CommentNotFoundException.byId(id));
        commentRepository.delete(comment);
    }
}
