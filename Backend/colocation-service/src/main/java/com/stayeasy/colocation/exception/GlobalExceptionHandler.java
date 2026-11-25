package com.stayeasy.colocation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequest(BadRequestException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflict(ConflictException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> unavailable(ServiceUnavailableException e) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .reduce((a, b) -> a + " ; " + b).orElse("Données invalides");
        return build(HttpStatus.BAD_REQUEST, msg);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message));
    }
}
