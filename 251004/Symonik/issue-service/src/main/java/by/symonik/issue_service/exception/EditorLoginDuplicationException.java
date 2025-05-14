package by.symonik.issue_service.exception;

public class EditorLoginDuplicationException extends RuntimeException {
    private EditorLoginDuplicationException(String message) {
        super(message);
    }

    public static EditorLoginDuplicationException byLogin(String login) {
        return new EditorLoginDuplicationException(String.format("Editor with login %s already exists.", login));
    }
}
