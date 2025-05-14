package by.symonik.comment_service.service;

import by.symonik.comment_service.dto.CommentUpdateRequestTo;
import by.symonik.comment_service.dto.CommentRequestTo;
import by.symonik.comment_service.dto.CommentResponseTo;

import java.util.List;

public interface CommentService {
    CommentResponseTo create(CommentRequestTo commentRequestTo);

    List<CommentResponseTo> readAll();

    CommentResponseTo readById(Long id);

    CommentResponseTo update(CommentUpdateRequestTo commentUpdateRequestTo);

    void deleteById(Long id);
}
