package by.symonik.issue_service.exception;

public class IssueNotFoundException extends RuntimeException {
    private IssueNotFoundException(String message) {
        super(message);
    }

    public static IssueNotFoundException byId(Long id) {
        return new IssueNotFoundException(String.format("Issue with id %d not found", id));
    }
}
