package com.mindex.challenge.exceptions;

/**
 * Exception thrown to indicate that a database access exception occurred, providing helpful messages.
 *
 * @author Robert Heinbokel.
 */
public class UnexpectedDatabaseException extends RuntimeException{

    /**
     * Constructs a new UnexpectedDatabaseException with the specified message and original cause.
     *
     * @param message the message providing about the exception.
     * @param cause the original cause of the exception.
     */
    public UnexpectedDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}