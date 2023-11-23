package com.okey.drone.exception;

import java.io.Serializable;

public class InvalidRequestException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Exception e) {
        super(e.getMessage(), e);
    }

    public InvalidRequestException(String message, Exception e) {
        super(message, e);
    }
}
