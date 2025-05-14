package by.ryabchikov.comment_service.mapper;

import by.ryabchikov.comment_service.dto.CommentRequestTo;
import by.ryabchikov.comment_service.dto.CommentResponseTo;
import by.ryabchikov.comment_service.entity.Comment;
import by.ryabchikov.data.CommentRequestEvent;
import by.ryabchikov.data.CommentResponseEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentRequestTo commentRequestTo);

    CommentResponseTo toCommentResponseTo(Comment comment);

    CommentRequestTo toCommentRequestTo(CommentRequestEvent commentRequestEvent);

    CommentResponseEvent toCommentResponseEvent(CommentResponseTo commentResponseTo);
}
