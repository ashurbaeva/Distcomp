package by.symonik.comment_service.mapper;

import by.symonik.comment_service.dto.CommentRequestTo;
import by.symonik.comment_service.dto.CommentResponseTo;
import by.symonik.comment_service.entity.Comment;
import by.symonik.data.CommentRequestEvent;
import by.symonik.data.CommentResponseEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentRequestTo commentRequestTo);

    CommentResponseTo toCommentResponseTo(Comment comment);

    CommentRequestTo toCommentRequestTo(CommentRequestEvent commentRequestEvent);

    CommentResponseEvent toCommentResponseEvent(CommentResponseTo commentResponseTo);
}
