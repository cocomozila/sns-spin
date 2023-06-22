package spin.sns.error.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
    }

    public DuplicateNicknameException(String message) {
        super(message);
    }
}
