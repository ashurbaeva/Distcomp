package by.symonik.issue_service.exception;

public class LabelNotFoundException extends RuntimeException {
    private LabelNotFoundException(String message) {
        super(message);
    }

    public static LabelNotFoundException byId(Long id) {
        return new LabelNotFoundException(String.format("Label with id %d not found", id));
    }
}
