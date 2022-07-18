package org.api.mocktests.models;

public class Header {

    private static String name;

    private static TypeHeader typeHeader;

    private static String[] values;

    public Header(String name, TypeHeader typeHeader, String... values) {
        Header.name = name;
        Header.typeHeader = typeHeader;
        Header.values = values;
    }

    public String getName() {
        return name;
    }

    public TypeHeader getTypeHeader() {
        return typeHeader;
    }

    public String[] getValues() {
        return values;
    }
}
