package spin.sns.error.exception;

public class MemberNotExistException extends RuntimeException {

    public MemberNotExistException() {
    }

    public MemberNotExistException(String message) {
        super(message);
    }
}
