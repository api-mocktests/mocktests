package org.api.mocktests.models;

public enum Operation {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete"),
    PATCH("patch");

    private String operation;

    Operation(String operation) {
        this.operation = operation;
    }
}
