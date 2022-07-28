package org.api.mocktests.models;

import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.utils.MockTest;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public final class Request {

    private static Operation operation;

    private static String endpoint;

    private static Header header;

    private static Object[] params;

    private static String contentType;

    private static Object body;

    private static MockTest mockUtilitaries;

    public Request(MockTest mockTest) {
        super();
        mockUtilitaries = mockTest;
    }

    public Request operation(Operation operation) throws Exception {
        Request.operation = operation;
        return this;
    }

    public Request endpoint(String endpoint) throws Exception {
        Request.endpoint = endpoint;
        return this;
    }

    public Request header(String name, TypeHeader typeHeader, String... values) {
        Request.header = new Header(name, typeHeader, values);
        return this;
    }

    public Request params(Object... params) {
        Request.params = params;
        return this;
    }

    public Request contentType(String contentType) {
        Request.contentType = contentType;
        return this;
    }

    public Request body(Object body) {
        Request.body = body;
        return this;
    }

    public RequestBuilder compileRequest() throws Exception {

        verifyOperation();
        verifyEndpoint();

        MockHttpServletRequestBuilder mockRequest = mockUtilitaries.convertOperation(operation, endpoint, params);
        if(contentType != null)
            mockRequest.contentType(contentType);
        if(body != null)
            mockRequest.content(mockUtilitaries.getObjectMapper().writeValueAsString(body));

        return mockRequest;
    }

    private void verifyOperation() throws InvalidRequestException {
        if(operation == null)
            throw new InvalidRequestException("operation not nullable");
    }


    private void verifyEndpoint() throws InvalidRequestException {
        if(endpoint == null)
            throw new InvalidRequestException("endpoint not nullable");
    }

    /*
    *  GETTERS
    */

    public Operation getOperation() {
        return operation;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Header getHeader() {
        return header;
    }

    public Object[] getParams() {
        return params;
    }

    public Object getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
