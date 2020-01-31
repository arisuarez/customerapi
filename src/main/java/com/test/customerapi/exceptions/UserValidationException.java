package com.test.customerapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserValidationException() {
        super("All fields are required!");
    }
}