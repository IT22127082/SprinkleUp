package com.sprinkleup.exception;

/** Thrown when a signed-in user opens a page their role may not use. */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
