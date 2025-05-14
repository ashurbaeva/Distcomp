package by.symonik.issue_service.entity.service;

import by.symonik.issue_service.dto.comment.CommentRequestTo;
import by.symonik.issue_service.dto.comment.CommentResponseTo;
import by.symonik.issue_service.dto.comment.CommentUpdateRequestTo;

import java.util.List;

public interface CommentService {
    CommentResponseTo create(CommentRequestTo commentRequestTo);

    List<CommentResponseTo> readAll();

    CommentResponseTo readById(Long id);

    CommentResponseTo update(CommentUpdateRequestTo commentUpdateRequestTo);

    void deleteById(Long id);
}
