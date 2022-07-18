package org.api.mocktests.exceptions;

public class NotImplementedRequestException extends RequestException {

    public NotImplementedRequestException() {
        super();
    }

    public NotImplementedRequestException(String message) {
        super(message);
    }
}
