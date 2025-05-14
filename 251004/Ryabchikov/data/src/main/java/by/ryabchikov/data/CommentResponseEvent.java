package by.ryabchikov.data;

public record CommentResponseEvent(
        Long id,
        Long tweetId,
        String content
) {
}
