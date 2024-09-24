package com.github.mahmoudalikhalil.elm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException exception, WebRequest request) {
        log.error("Caught data integrity violation", exception);
        return createProblemDetail(exception, HttpStatus.BAD_REQUEST, "Data integrity violation", null, null, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        log.error("Caught bad credentials error", exception);
        return createProblemDetail(exception, HttpStatus.UNAUTHORIZED, "Bad Credentials.", null, null, request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception exception, WebRequest request) {
        log.error("Unexpected error occurred", exception);
        return createProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", null, null, request);
    }
}
