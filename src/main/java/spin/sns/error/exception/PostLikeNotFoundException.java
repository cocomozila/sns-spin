package spin.sns.error.exception;

public class PostLikeNotFoundException extends RuntimeException {

    public PostLikeNotFoundException() {
    }

    public PostLikeNotFoundException(String message) {
        super(message);
    }
}
