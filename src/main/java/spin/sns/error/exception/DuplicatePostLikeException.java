package spin.sns.error.exception;

public class DuplicatePostLikeException extends RuntimeException {

    public DuplicatePostLikeException() {
    }

    public DuplicatePostLikeException(String message) {
        super(message);
    }
}
