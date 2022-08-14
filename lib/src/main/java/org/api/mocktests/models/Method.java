package org.api.mocktests.models;

public enum Method {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete"),
    PATCH("patch");

    private String method;

    Method(String method) {
        this.method = method;
    }
}
