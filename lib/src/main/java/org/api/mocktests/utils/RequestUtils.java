package org.api.mocktests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.exceptions.NotImplementedRequestException;
import org.api.mocktests.extensions.AuthenticateExtension;
import org.api.mocktests.extensions.AuthenticatedTestExtension;
import org.api.mocktests.extensions.AutoConfigureRequestExtension;
import org.api.mocktests.models.Header;
import org.api.mocktests.models.Operation;
import org.api.mocktests.models.TypeHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public final class RequestUtils {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Object object;

    private final AuthenticateExtension authenticateExtension = new AuthenticateExtension();
    private final AuthenticatedTestExtension authenticatedTestExtension = new AuthenticatedTestExtension();

    private final AutoConfigureRequestExtension autoConfigureRequestExtension = new AutoConfigureRequestExtension();

    public RequestUtils() {
        super();
        object = this.getClass(this.getCurrentMethod());
    }

    public ResultActions invokeLogin() {
        return authenticateExtension.invokeMethodLogin(object);
    }
    public boolean methodIsAnnotAuthTest() {

        //String[] methodsStack = authenticatedTestExtension.getMethods();
        StackTraceElement ste = authenticatedTestExtension.getMethods();
        List<String> methodsAuthenticatedTest = authenticatedTestExtension.getMethodsAuthenticatedTest(object);

        return methodsAuthenticatedTest.contains(ste.getMethodName());
    }

    public StackTraceElement getCurrentMethod() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public Class<?> getClass(StackTraceElement stackTraceElement) {

        String className = stackTraceElement.getClassName();
        String[] fileClassName = className.split("\\.");
        try {
            return ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
            /*try {
                return ClassLoader.getPlatformClassLoader().loadClass(fileClassName[fileClassName.length - 1]);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoader.getSystemClassLoader().loadClass(fileClassName[fileClassName.length - 1]);
                } catch (ClassNotFoundException exc) {
                    throw new RuntimeException(exc);
                }
            }*/
        }
    }

    public boolean verifyAnnotAutoConfigureContext() {
        return autoConfigureRequestExtension.classIsAnnotAutoConfigureContext(object);
    }

    public boolean verifyAnnotAutoConfigureHeader() {
        return autoConfigureRequestExtension.classIsAnnotAutoConfigureHeader(object);
    }

    public String getAutoConfigureContextType() throws InvalidRequestException {
        return autoConfigureRequestExtension.getAutoConfigureContextType(object);
    }

    public String[] getAutoConfigureHeader() throws InvalidRequestException {
        return autoConfigureRequestExtension.getAutoConfigureHeader(object);
    }

    public boolean verifyMethodLogin() {
        return authenticateExtension.methodLoginIsIstantiated(object);
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

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
