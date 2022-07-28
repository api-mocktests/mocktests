package org.api.mocktests.models;

import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.utils.RequestUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public final class Request {

    private static Operation operation;

    private static String endpoint;

    private static Header header;

    private static Object[] params;

    private static MediaType contentType;

    private static Object body;

    private static RequestUtils requestUtils;

    public Request(RequestUtils requestUtils) {
        super();
        this.requestUtils = requestUtils;
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

    public Request contentType(MediaType contentType) {
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

        MockHttpServletRequestBuilder mockRequest = requestUtils.convertOperation(operation, endpoint, params);

        if(header == null) {
            
        }
        else {
            mockRequest.header(header.getName(), requestUtils.convertTypeHeaders(header));
        }

        if(contentType != null)
            mockRequest.contentType(contentType);
        if(body != null)
            mockRequest.content(requestUtils.getObjectMapper().writeValueAsString(body));

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
}
