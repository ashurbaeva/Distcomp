package by.symonik.issue_service.exception;

public class IssueTitleDuplicationException extends RuntimeException {
    private IssueTitleDuplicationException(String message) {
        super(message);
    }

    public static IssueTitleDuplicationException byTitle(String title) {
        return new IssueTitleDuplicationException(String.format("Issue with title %s already exists.", title));
    }
}