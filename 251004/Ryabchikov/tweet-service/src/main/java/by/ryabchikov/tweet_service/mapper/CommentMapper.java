package by.ryabchikov.tweet_service.mapper;

import by.ryabchikov.data.CommentRequestEvent;
import by.ryabchikov.data.CommentResponseEvent;
import by.ryabchikov.tweet_service.dto.comment.CommentRequestTo;
import by.ryabchikov.tweet_service.dto.comment.CommentResponseTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentRequestEvent toCommentRequestEvent(CommentRequestTo commentRequestTo);

    CommentResponseTo toCommentResponseTo(CommentResponseEvent commentResponseEvent);
}
