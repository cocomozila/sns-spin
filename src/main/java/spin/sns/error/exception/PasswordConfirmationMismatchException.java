package spin.sns.error.exception;

public class PasswordConfirmationMismatchException extends RuntimeException {

    public PasswordConfirmationMismatchException() {
    }

    public PasswordConfirmationMismatchException(String message) {
        super(message);
    }
}
