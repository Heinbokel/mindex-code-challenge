package com.mindex.challenge.config;

import com.mindex.challenge.exceptions.CircularReferenceException;
import com.mindex.challenge.exceptions.DuplicateEntityException;
import com.mindex.challenge.exceptions.ErrorDetails;
import com.mindex.challenge.exceptions.ResourceNotFoundException;
import com.mindex.challenge.exceptions.UnexpectedDatabaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

/**
 * GlobalControllerExceptionHandler handles exceptions for all controllers.
 * This ensures all exceptions will be handled before responding to the client.
 *
 * @author Robert Heinbokel.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Handles MethodArgumentNotValidException
     * @param ex the {@link MethodArgumentNotValidException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors,
                request.getRequestURI()
        );
        LOG.error("MethodArgumentNotValidException was thrown: {}", errorDetails.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * Handles HandlerMethodValidationException
     * @param ex the {@link HandlerMethodValidationException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDetails> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error in one or more request parameters",
                ex.getMessage(),
                request.getRequestURI()
        );

        LOG.error("HandlerMethodValidationException was thrown: {}", errorDetails.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * Handles ConstraintViolationException
     * @param ex the {@link ConstraintViolationException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        String errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors,
                request.getRequestURI()
        );
        LOG.error("ConstraintViolationException was thrown: {}", errorDetails.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * Handles DuplicateEntityException
     * @param ex the {@link DuplicateEntityException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateEntityException(DuplicateEntityException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                "Unique constraint violation",
                ex.getMessage(),
                request.getRequestURI()
        );
        LOG.error("DuplicateEntityException was thrown: {}", errorDetails.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * Handles ResourceNotFoundException
     * @param ex the {@link ResourceNotFoundException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                ex.getMessage(),
                request.getRequestURI()
        );
        LOG.error("ResourceNotFoundException was thrown: {}", errorDetails.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    /**
     * Handles UnexpectedDatabaseException
     * @param ex the {@link UnexpectedDatabaseException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(UnexpectedDatabaseException.class)
    public ResponseEntity<ErrorDetails> handleUnexpectedDatabaseException(UnexpectedDatabaseException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected database error",
                ex.getMessage(),
                request.getRequestURI()
        );
        LOG.error("UnexpectedDatabaseException was thrown: {}", errorDetails.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    /**
     * Handles CircularReferenceException
     * @param ex the {@link CircularReferenceException} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(CircularReferenceException.class)
    public ResponseEntity<ErrorDetails> handleCircularReferenceException(CircularReferenceException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                "An unexpected error occurred while processing the request", // Intentionally vague message
                request.getRequestURI()
        );
        // Log the error for ourselves to see, but don't let the caller know the specific cause.
        LOG.error("CircularReferenceException was thrown: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    /**
     * Handles all other exceptions not caught elsewhere.
     * @param ex the {@link Exception} to handle.
     * @param request the {@link HttpServletRequest} containing the request/path data.
     * @return the {@link ResponseEntity<ErrorDetails>} to return.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                ex.getMessage(),
                request.getRequestURI()
        );
        LOG.error("The following error occurred: {} {}", ex.getMessage(), ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}