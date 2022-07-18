package org.api.mocktests.exceptions;

public class InvalidRequestException extends RequestException {

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
