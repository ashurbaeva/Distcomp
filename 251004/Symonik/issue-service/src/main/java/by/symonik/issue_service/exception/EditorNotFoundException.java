package by.symonik.issue_service.exception;

public class EditorNotFoundException extends RuntimeException {
    private EditorNotFoundException(String message) {
        super(message);
    }

    public static EditorNotFoundException byId(Long id) {
        return new EditorNotFoundException(String.format("Editor with id %d not found", id));
    }
}
