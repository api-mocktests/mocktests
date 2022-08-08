package org.api.mocktests.models;

import org.api.mocktests.exceptions.InvalidRequestException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public final class Request {

    private Operation operation;

    private String endpoint;

    private Header header;

    private Object[] pathParams;

    private MultiValueMap<String, String> params;

    private String[] param;

    private MediaType contentType;

    private Object body;

    public Request() {
        super();
    }

    public Request operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public Request endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public Request header(String name, TypeHeader typeHeader, String... values) {
        this.header = new Header(name, typeHeader, values);
        return this;
    }

    public Request pathParams(Object... pathParams) {
        this.pathParams = pathParams;
        return this;
    }

    public Request params(MultiValueMap<String, String> params) {
        this.params = params;
        return this;
    }

    public Request param(String... param) {
        this.param = param;
        return this;
    }

    public Request contentType(MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    public Request body(Object body) {
        this.body = body;
        return this;
    }

    public void verifyOperation() throws InvalidRequestException {
        if(operation == null)
            throw new InvalidRequestException("operation not nullable");
    }


    public void verifyEndpoint() throws InvalidRequestException {
        if(endpoint == null)
            throw new InvalidRequestException("endpoint not nullable");
    }

    public Operation getOperation() {
        return operation;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Object[] getPathParams() {
        return pathParams;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public String[] getParam() {
        return param;
    }

    public Header getHeader() {
        return header;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public Object getBody() {
        return body;
    }
}
