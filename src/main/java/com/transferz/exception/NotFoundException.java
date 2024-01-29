package com.transferz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public String resource;

    public NotFoundException(String resource) {
        super(HttpStatus.NOT_FOUND, resource);
        this.resource = resource;
    }
}
