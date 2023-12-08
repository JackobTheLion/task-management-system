package ru.yakovlev.kanban.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yakovlev.kanban.exception.exceptions.AccessDeniedException;
import ru.yakovlev.kanban.exception.exceptions.EmailOrNameRegisteredException;
import ru.yakovlev.kanban.exception.exceptions.NotFoundException;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(RuntimeException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentials(RuntimeException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({EmailOrNameRegisteredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmailOrNameRegisteredException(RuntimeException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(RuntimeException e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
