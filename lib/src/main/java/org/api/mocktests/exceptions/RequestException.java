package org.api.mocktests.exceptions;

public class RequestException extends Exception {

    public RequestException() {
        super();
    }

    public RequestException(String message) {
        super(message);
    }
}
