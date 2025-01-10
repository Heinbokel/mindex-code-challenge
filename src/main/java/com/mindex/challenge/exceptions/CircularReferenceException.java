package com.mindex.challenge.exceptions;

/**
 * Exception thrown to indicate that a circular reference caused an error.
 *
 * @author Robert Heinbokel.
 */
public class CircularReferenceException extends RuntimeException{

    /**
     * Constructs a new CircularReferenceException with the specified message.
     *
     * @param message the message providing about the exception.
     */
    public CircularReferenceException(String message) {
        super(message);
    }
}