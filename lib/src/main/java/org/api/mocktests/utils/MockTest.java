package org.api.mocktests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.exceptions.NotImplementedRequestException;
import org.api.mocktests.extensions.AuthenticateExtension;
import org.api.mocktests.extensions.AuthenticatedTestExtension;
import org.api.mocktests.extensions.AutoConfigureRequestExtension;
import org.api.mocktests.models.Header;
import org.api.mocktests.models.Operation;
import org.api.mocktests.models.Request;
import org.api.mocktests.models.TypeHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public final class MockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Class<?> aClass;
    @Autowired
    private AuthenticateExtension authenticateExtension;

    @Autowired
    private AuthenticatedTestExtension authenticatedTestExtension;

    @Autowired
    private AutoConfigureRequestExtension autoConfigureRequestExtension;

    public ResultActions performRequest(Request request) throws Exception {

        this.getClass(this.getCurrentMethod());
        request.verifyOperation();
        request.verifyEndpoint();

        MockHttpServletRequestBuilder mockRequest = convertOperation(request.getOperation(), request.getEndpoint(), request.getPathParams());

        if(request.getParams() != null)
            mockRequest.params(request.getParams());

        if(request.getParam() != null)
            mockRequest.param(request.getParam()[0], request.getParam()[1]);

        if(request.getHeader() == null) {
            if(verifyMethodLogin() && methodIsAnnotAuthTest()) {

                ResultActions resultLogin = invokeLogin();
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
            else if (verifyAnnotAutoConfigureHeader() && methodIsAnnotAuthTest()) {
                String[] headerValues = getAutoConfigureHeader();
                if(headerValues.length < 2)
                    throw new InvalidRequestException("invalid auto configure header");
                mockRequest.header(headerValues[0],headerValues[1]);
            }
        }
        else {
            mockRequest.header(request.getHeader().getName(), convertTypeHeaders(request.getHeader()));
        }

        if(request.getContentType() == null && verifyAnnotAutoConfigureContext()) {
            mockRequest.contentType(getAutoConfigureContextType());
        }
        else
            mockRequest.contentType(request.getContentType());

        if(request.getBody() != null)
            mockRequest.content(objectMapper.writeValueAsString(request.getBody()));


        return mockMvc.perform(mockRequest);
    }
    public ResultActions invokeLogin() {
        return authenticateExtension.invokeMethodLogin(aClass);
    }
    public boolean methodIsAnnotAuthTest() {

        //String[] methodsStack = authenticatedTestExtension.getMethods();
        StackTraceElement ste = authenticatedTestExtension.getMethods();
        List<String> methodsAuthenticatedTest = authenticatedTestExtension.getMethodsAuthenticatedTest(aClass);

        return methodsAuthenticatedTest.contains(ste.getMethodName());
    }

    public StackTraceElement getCurrentMethod() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        return Thread.currentThread().getStackTrace()[4];
    }

    public Class<?> getClass(StackTraceElement stackTraceElement) {

        String className = stackTraceElement.getClassName();
        try {
            return ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyAnnotAutoConfigureContext() {
        return autoConfigureRequestExtension.classIsAnnotAutoConfigureContext(aClass);
    }

    public boolean verifyAnnotAutoConfigureHeader() {
        return autoConfigureRequestExtension.classIsAnnotAutoConfigureHeader(aClass);
    }

    public String getAutoConfigureContextType() throws InvalidRequestException {
        return autoConfigureRequestExtension.getAutoConfigureContextType(aClass);
    }

    public String[] getAutoConfigureHeader() throws InvalidRequestException {
        return autoConfigureRequestExtension.getAutoConfigureHeader(aClass);
    }

    public boolean verifyMethodLogin() {
        return authenticateExtension.methodLoginIsIstantiated(aClass);
    }

    public String convertTypeHeaders(Header header) throws NotImplementedRequestException {

        if(header.getTypeHeader().equals(TypeHeader.BEARER)) {
            return String.format("%s %s", header.getTypeHeader().getTypeHeader(), header.getValues()[0]);
        }

        throw new NotImplementedRequestException(String.format("Type header %s not implemented!",header.getName()));
    }

    public MockHttpServletRequestBuilder convertOperation(Operation operation, String endpoint, Object[] params) {

        if(operation.equals(Operation.GET)) {
            if(params == null || params.length == 0)
                return get(endpoint);
            return get(endpoint, params);
        }

        if(operation.equals(Operation.POST)) {
            if(params == null || params.length == 0)
                return post(endpoint);
            return post(endpoint, params);
        }

        if(operation.equals(Operation.PUT)) {
            if(params == null || params.length == 0)
                return put(endpoint);
            return put(endpoint, params);
        }

        if(operation.equals(Operation.DELETE)) {
            if(params == null || params.length == 0)
                return delete(endpoint);
            return delete(endpoint, params);
        }

        if(params == null || params.length == 0)
            return patch(endpoint);
        return patch(endpoint, params);
    }
}
