package com.stayeasy.maintenance.exception;

/** Service distant indisponible (503) */
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
