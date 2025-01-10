package com.mindex.challenge.exceptions;

/**
 * Exception thrown to indicate that a duplicate entry occurred, which would violate business logic uniqueness.
 *
 * @author Robert Heinbokel.
 */
public class DuplicateEntityException extends RuntimeException{

    /**
     * Constructs a new DuplicateEntityException with the specified message.
     *
     * @param message the message providing about the exception.
     */
    public DuplicateEntityException(String message) {
        super(message);
    }
}