package org.api.mocktests.models;

public enum TypeHeader {
    BEARER("Bearer");

    private final String typeHeader;

    TypeHeader(String typeHeader) {
        this.typeHeader = typeHeader;
    }

    public String getTypeHeader() {
        return typeHeader;
    }
}
