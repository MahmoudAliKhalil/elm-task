package com.github.mahmoudalikhalil.elm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class AdminSelfUpdateException extends ErrorResponseException {
    public AdminSelfUpdateException() {
        super(HttpStatus.CONFLICT);
    }
}
