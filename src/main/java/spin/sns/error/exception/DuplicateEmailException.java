package spin.sns.error.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
