package by.symonik.data;

public record CommentRequestEvent(
        Long issueId,
        String content
) {
}
