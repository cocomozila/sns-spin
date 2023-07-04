package spin.sns.error.exception;

public class DuplicateFollowException extends RuntimeException {

    public DuplicateFollowException() {
    }

    public DuplicateFollowException(String message) {
        super(message);
    }
}
