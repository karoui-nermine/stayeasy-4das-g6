package com.stayeasy.colocation.exception;

/** Conflit métier (409) */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
