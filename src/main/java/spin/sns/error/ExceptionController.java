package spin.sns.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spin.sns.error.exception.DuplicateEmailException;
import spin.sns.error.exception.DuplicateNicknameException;
import spin.sns.error.exception.MemberNotExistException;
import spin.sns.error.exception.PasswordMismatchException;

@RestControllerAdvice
public class ExceptionController {

    private static String getSimpleName(Exception e) {
        return e.getClass().getSimpleName();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResult(e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), getSimpleName(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberNotExistException.class)
    public ErrorResult handleMemberNotExistException(MemberNotExistException e) {
        return new ErrorResult(e.getLocalizedMessage(), getSimpleName(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordMismatchException.class)
    public ErrorResult handlePasswordMismatchException(PasswordMismatchException e) {
        return new ErrorResult(e.getLocalizedMessage(), getSimpleName(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateNicknameException.class)
    public ErrorResult handleDuplicateNicknameException(DuplicateNicknameException e) {
        return new ErrorResult(e.getLocalizedMessage(), getSimpleName(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResult handleDuplicateEmailException(DuplicateEmailException e) {
        return new ErrorResult(e.getLocalizedMessage(), getSimpleName(e));
    }
}