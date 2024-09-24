package com.github.mahmoudalikhalil.elm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ProductNotFoundException extends ErrorResponseException {
    public ProductNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
