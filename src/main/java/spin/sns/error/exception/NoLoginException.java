package spin.sns.error.exception;

public class NoLoginException extends RuntimeException {

    public NoLoginException() {
    }

    public NoLoginException(String message) {
        super(message);
    }
}
