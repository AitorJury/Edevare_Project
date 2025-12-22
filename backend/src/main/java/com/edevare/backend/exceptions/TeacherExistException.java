package com.edevare.backend.exceptions;

public class TeacherExistException extends RuntimeException {
    public TeacherExistException(String message) {
        super(message);
    }
}
