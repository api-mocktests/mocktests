package org.api.mocktests.exceptions;

public class UnauthorizedRequestException extends RequestException {

    public UnauthorizedRequestException() {
        super();
    }

    public UnauthorizedRequestException(String message) {
        super(message);
    }
}
