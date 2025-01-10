package com.mindex.challenge.config;

import com.mindex.challenge.exceptions.CircularReferenceException;
import com.mindex.challenge.exceptions.DuplicateEntityException;
import com.mindex.challenge.exceptions.ErrorDetails;
import com.mindex.challenge.exceptions.ResourceNotFoundException;
import com.mindex.challenge.exceptions.UnexpectedDatabaseException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Uni tests for {@link GlobalControllerExceptionHandler}.
 *
 * @author Robert Heinbokel
 */
public class GlobalControllerExceptionHandlerTest {

    private GlobalControllerExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @Before
    public void setUp() {
        exceptionHandler = new GlobalControllerExceptionHandler();
        request = mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    public void testHandleConstraintViolationException() {
        ConstraintViolation<String> violation = mock(ConstraintViolation.class);
        Mockito.when(violation.getMessage()).thenReturn("must not be null");

        ConstraintViolationException exception = new ConstraintViolationException("Validation error", Set.of(violation));
        ResponseEntity<ErrorDetails> response = exceptionHandler.handleConstraintViolationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("Validation error", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testHandleDuplicateEntityException() {
        DuplicateEntityException exception = new DuplicateEntityException("Duplicate entity found");

        ResponseEntity<ErrorDetails> response = exceptionHandler.handleDuplicateEntityException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("Unique constraint violation", response.getBody().getMessage());
        assertEquals("Duplicate entity found", response.getBody().getError());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        ResponseEntity<ErrorDetails> response = exceptionHandler.handleResourceNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    public void testHandleUnexpectedDatabaseException() {
        UnexpectedDatabaseException exception = new UnexpectedDatabaseException("Database error", new Exception("Cause exception"));

        ResponseEntity<ErrorDetails> response = exceptionHandler.handleUnexpectedDatabaseException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("Unexpected database error", response.getBody().getMessage());
        assertEquals("Database error", response.getBody().getError());
    }

    @Test
    public void testHandleCircularReferenceException() {
        CircularReferenceException exception = new CircularReferenceException("Circular reference detected");

        ResponseEntity<ErrorDetails> response = exceptionHandler.handleCircularReferenceException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    @Test
    public void testHandleGlobalException() {
        Exception exception = new Exception("Global error");

        ResponseEntity<ErrorDetails> response = exceptionHandler.handleGlobalException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals("Global error", response.getBody().getError());
    }
}
