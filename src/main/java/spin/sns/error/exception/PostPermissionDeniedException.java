package spin.sns.error.exception;

public class PostPermissionDeniedException extends RuntimeException {

    public PostPermissionDeniedException() {
    }

    public PostPermissionDeniedException(String message) {
        super(message);
    }
}
