package com.example.recette.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.recette.local/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("resourceName", ex.getResourceName());
        problemDetail.setProperty("fieldName", ex.getFieldName());
        problemDetail.setProperty("fieldValue", ex.getFieldValue());
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Integrity Constraint Violation");
        problemDetail.setType(URI.create("https://api.recette.local/errors/conflict"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("resourceName", ex.getResourceName());
        problemDetail.setProperty("fieldName", ex.getFieldName());
        problemDetail.setProperty("fieldValue", ex.getFieldValue());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for request content.");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://api.recette.local/errors/bad-request"));
        problemDetail.setProperty("timestamp", Instant.now());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        problemDetail.setProperty("errors", fieldErrors);

        return problemDetail;
    }
}
