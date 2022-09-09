package org.api.mocktests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.exceptions.NotImplementedRequestException;
import org.api.mocktests.extensions.AuthenticateExtension;
import org.api.mocktests.extensions.AuthenticatedTestExtension;
import org.api.mocktests.extensions.AutoConfigureRequestExtension;
import org.api.mocktests.models.Header;
import org.api.mocktests.models.Method;
import org.api.mocktests.models.Request;
import org.api.mocktests.models.TypeHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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

    private void configureHeader(MockHttpServletRequestBuilder mockRequest) throws Exception {
        if(verifyFieldLogin() && methodIsAnnotAuthTest()) {

            Request requestLogin = getFieldLogin();
            ResultActions resultLogin = mockMvc.perform(convertRequestLogin(requestLogin));
            assert resultLogin != null;
            MockHttpServletResponse response = resultLogin.andReturn().getResponse();
            if (response.getStatus() >= 200 && response.getStatus() < 300) {
                if (response.getContentAsString().isEmpty() || response.getContentAsString().isBlank()) {
                    String token = response.getHeader("Authorization");
                    assert token != null;
                    if (token.startsWith("Authorization:")) {
                        String[] values = token.split(": ");
                        token = values[1];
                    }
                    mockRequest.header("Authorization", token);
                } else {
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
    public ResultActions performRequest(Request request) throws Exception {

        aClass = getClass(getCurrentMethod());
        request.verifyMethod();
        request.verifyUrl();

        MockHttpServletRequestBuilder mockRequest = convertOperation(request.getMethod(), request.getUrl(), request.getPathParams());

        if(request.getParams() != null)
            mockRequest.params(request.getParams());

        if(request.getHeader() == null) {
            configureHeader(mockRequest);
        }
        else if(request.getHeader().getName() == null) {
            Header header = configureRequestHeader(request);
            mockRequest.header(header.getName(), convertTypeHeaders(header));
        }
        else {
            mockRequest.header(request.getHeader().getName(), convertTypeHeaders(request.getHeader()));
        }

        if(request.getContentType() == null && verifyAnnotAutoConfigureContext()) {
            mockRequest.contentType(getAutoConfigureContextType());
        }
        else
            mockRequest.contentType(request.getContentType());

        if(request.getJson() != null)
            mockRequest.content(request.getJson());
        else if(request.getBody() != null)
            mockRequest.content(objectMapper.writeValueAsString(request.getBody()));

        return mockMvc.perform(mockRequest);
    }

    private Header configureRequestHeader(Request request) throws InvalidRequestException {

        String[] headerValues = getAutoConfigureHeader();
        if(headerValues.length < 2)
            throw new InvalidRequestException("invalid auto configure header");

        if(headerValues[1].contains("Bearer")) {
            return new Header(headerValues[0], TypeHeader.BEARER, request.getHeader().getValues());
        }
        throw new InvalidRequestException("invalid auto configure header");
    }

    private MockHttpServletRequestBuilder convertRequestLogin(Request req) throws Exception {

        req.verifyUrl();
        req.verifyMethod();
        MockHttpServletRequestBuilder reqReturn = convertOperation(req.getMethod(), req.getUrl(), req.getPathParams());

        if(req.getParams() != null)
            reqReturn.params(req.getParams());

        if(req.getContentType() == null && verifyAnnotAutoConfigureContext()) {
            reqReturn.contentType(getAutoConfigureContextType());
        }
        else {
            reqReturn.contentType(req.getContentType());
        }

        if(req.getBody() != null) {
            reqReturn.content(objectMapper.writeValueAsString(req.getBody()));
        }

        return reqReturn;
    }
    public Request getFieldLogin() {
        return authenticateExtension.getFieldLogin(aClass);
    }
    public boolean methodIsAnnotAuthTest() {

        StackTraceElement ste = authenticatedTestExtension.getMethods();
        List<String> methodsAuthenticatedTest = authenticatedTestExtension.getMethodsAuthenticatedTest(aClass);

        return methodsAuthenticatedTest.contains(ste.getMethodName());
    }

    public StackTraceElement getCurrentMethod() {
        return Thread.currentThread().getStackTrace()[3];
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

    public boolean verifyFieldLogin() {
        return authenticateExtension.fieldLoginIsIstantiated(aClass);
    }

    public String convertTypeHeaders(Header header) throws NotImplementedRequestException {

        if(header.getTypeHeader().equals(TypeHeader.BEARER)) {
            return String.format("%s %s", header.getTypeHeader().getTypeHeader(), header.getValues()[0]);
        }

        throw new NotImplementedRequestException(String.format("Type header %s not implemented!",header.getName()));
    }

    public MockHttpServletRequestBuilder convertOperation(Method method, String endpoint, Object[] params) {

        if(method.equals(Method.GET)) {
            if(params == null || params.length == 0)
                return get(endpoint);
            return get(endpoint, params);
        }

        if(method.equals(Method.POST)) {
            if(params == null || params.length == 0)
                return post(endpoint);
            return post(endpoint, params);
        }

        if(method.equals(Method.PUT)) {
            if(params == null || params.length == 0)
                return put(endpoint);
            return put(endpoint, params);
        }

        if(method.equals(Method.DELETE)) {
            if(params == null || params.length == 0)
                return delete(endpoint);
            return delete(endpoint, params);
        }

        if(params == null || params.length == 0)
            return patch(endpoint);
        return patch(endpoint, params);
    }
}
