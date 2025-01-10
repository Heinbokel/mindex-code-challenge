package com.mindex.challenge.exceptions;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents error information to be returned as a client request response when an exception occurs.
 *
 * @author Robert Heinbokel.
 */
public class ErrorDetails {
    private int status;
    private String error, message, path, timestamp;

    /**
     * Full args constructor
     * @param status the HTTP Status as an int.
     * @param message The friendly message of the error.
     * @param error The error's message.
     * @param path The URI where the error occurred.
     */
    public ErrorDetails(int status, String message, String error, String path) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    // Getters and Setters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}