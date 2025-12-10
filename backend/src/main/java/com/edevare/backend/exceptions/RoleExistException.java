package com.edevare.backend.exceptions;

public class RoleExistException extends RuntimeException {
    public RoleExistException(String message) {
        super(message);
    }
}
