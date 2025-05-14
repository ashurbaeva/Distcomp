package by.symonik.issue_service.mapper;

import by.symonik.data.CommentRequestEvent;
import by.symonik.data.CommentResponseEvent;
import by.symonik.issue_service.dto.comment.CommentRequestTo;
import by.symonik.issue_service.dto.comment.CommentResponseTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentRequestEvent toCommentRequestEvent(CommentRequestTo commentRequestTo);

    CommentResponseTo toCommentResponseTo(CommentResponseEvent commentResponseEvent);
}
