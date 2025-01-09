package com.mindex.challenge.exceptions;

/**
 * Exception thrown to indicate that a requested resource could not be found.
 *
 * @author Robert Heinbokel.
 */
public class ResourceNotFoundException extends RuntimeException{

    /**
     * Constructs a new ResourceNotFoundException with the specified message.
     *
     * @param message the message providing about the exception.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified message and original cause.
     *
     * @param message the message providing about the exception.
     * @param cause the original cause of the exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
