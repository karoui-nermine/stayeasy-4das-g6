package com.stayeasy.maintenance.exception;

/** Requête invalide (400) */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
