package org.api.mocktests.models;

import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.utils.RequestUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public final class Request {

    private Operation operation;

    private String endpoint;

    private Header header;

    private Object[] params;

    private MediaType contentType;

    private Object body;

    private final RequestUtils requestUtils;

    public Request(RequestUtils requestUtils) {
        super();
        this.requestUtils = requestUtils;
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

    public Request params(Object... params) {
        this.params = params;
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

    public RequestBuilder compileRequest() throws Exception {

        verifyOperation();
        verifyEndpoint();

        MockHttpServletRequestBuilder mockRequest = requestUtils.convertOperation(operation, endpoint, params);

        if(header == null) {
            if(requestUtils.verifyMethodLogin() && requestUtils.methodIsAnnotAuthTest()) {

                ResultActions resultLogin = requestUtils.invokeLogin();
                assert resultLogin != null;
                MockHttpServletResponse response = resultLogin.andReturn().getResponse();
                if(response.getStatus() >= 200 && response.getStatus() < 300) {
                    if(response.getContentAsString().isEmpty() || response.getContentAsString().isBlank()) {
                        String token = response.getHeader("Authorization");
                        assert token != null;
                        if(token.startsWith("Authorization:")) {
                            String[] values = token.split(": ");
                            token = values[1];
                        }
                        mockRequest.header("Authorization", token);
                    }
                    else {
                        String tokenResponse = response.getContentAsString();
                        String[] values = tokenResponse.split(":");
                        mockRequest.header("Authorization", "Bearer " + values[1].split("\"")[1]);
                    }
                }
            }
            else if (requestUtils.verifyAnnotAutoConfigureHeader()) {
                String[] headerValues = requestUtils.getAutoConfigureHeader();
                if(headerValues.length < 2)
                    throw new InvalidRequestException("invalid auto configure header");
                mockRequest.header(headerValues[0],headerValues[1]);
            }
        }
        else {
            mockRequest.header(header.getName(), requestUtils.convertTypeHeaders(header));
        }

        if(contentType == null && requestUtils.verifyAnnotAutoConfigureContext()) {
            mockRequest.contentType(requestUtils.getAutoConfigureContextType());
        }
        else
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
