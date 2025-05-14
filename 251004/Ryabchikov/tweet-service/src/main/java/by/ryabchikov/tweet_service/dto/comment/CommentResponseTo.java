package by.ryabchikov.tweet_service.dto.comment;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record CommentResponseTo (
        Long id,
        Long tweetId,
        String content
) implements Serializable {
}
