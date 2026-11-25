package com.stayeasy.facturation.exception;

/** Ressource introuvable (404) */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
