package com.transferz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {
    public String reason;

    public ConflictException(String reason) {
        super(HttpStatus.CONFLICT, reason);
        this.reason = reason;
    }
}
