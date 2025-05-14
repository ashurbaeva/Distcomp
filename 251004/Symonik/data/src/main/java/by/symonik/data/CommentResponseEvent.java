package by.symonik.data;

public record CommentResponseEvent(
        Long id,
        Long issueId,
        String content
) {
}
