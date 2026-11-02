package com.sprinkleup.exception;

/** Thrown when a record with the requested id does not exist. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
