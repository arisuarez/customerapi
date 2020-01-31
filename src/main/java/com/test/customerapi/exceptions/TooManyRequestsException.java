package com.test.customerapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestsException extends RuntimeException {
    private static final long serialVersionUID = 2L;

    public TooManyRequestsException() {
        super("Too many requests! Only 3 requests are allowed every 10 seconds.");
    }
}