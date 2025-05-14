package by.ryabchikov.data;

public record CommentRequestEvent(
        Long tweetId,
        String content
) {
}