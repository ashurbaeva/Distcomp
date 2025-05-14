package by.symonik.comment_service.exception;

public class CommentNotCreatedException extends RuntimeException {
    private CommentNotCreatedException(String message) {
        super(message);
    }

    public static CommentNotCreatedException byInvalidIssueId(Long issueId) {
        return new CommentNotCreatedException(String.format("Comment not created, because issue with id: %d not found", issueId));
    }
}
