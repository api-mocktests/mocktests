package org.api.mocktests.models;

import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.utils.RequestUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
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
        Request.requestUtils = requestUtils;
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
            if(requestUtils.verifyMethodLogin() && requestUtils.methodIsAnnotAuthTest()) {

                ResultActions resultLogin = requestUtils.invokeLogin();
                if(resultLogin == null)
                    System.out.println("resultLogin is null");
                MockHttpServletResponse response = resultLogin.andReturn().getResponse();
                if(response.getStatus() >= 200 && response.getStatus() < 300) {
                    String tokenResponse = response.getContentAsString();
                    String[] values = tokenResponse.split(":");
                    mockRequest.header("Authorization", "Bearer " + values[1].split("\"")[1]);
                }
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
