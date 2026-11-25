package com.stayeasy.facturation.exception;

/** Conflit métier (409) */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
