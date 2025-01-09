package com.mindex.challenge.exceptions;

/**
 * Represents error information to be returned as a client request response when an exception occurs.
 *
 * @author Robert Heinbokel.
 */
public class ErrorDetails {
    private int status;
    private String message, errors;

    public ErrorDetails(int status, String message, String errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
