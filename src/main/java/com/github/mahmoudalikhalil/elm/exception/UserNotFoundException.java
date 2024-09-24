package com.github.mahmoudalikhalil.elm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class UserNotFoundException extends ErrorResponseException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
