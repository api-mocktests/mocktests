package org.api.mocktests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.exceptions.NotImplementedRequestException;
import org.api.mocktests.extensions.AuthenticateExtension;
import org.api.mocktests.extensions.AuthenticatedTestExtension;
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

    private static Object object;

    private final AuthenticateExtension authenticateExtension = new AuthenticateExtension();
    private final AuthenticatedTestExtension authenticatedTestExtension = new AuthenticatedTestExtension(object);

    public RequestUtils(Object object) {
        super();
        RequestUtils.object = object;
    }

    public ResultActions invokeLogin() {
        return authenticateExtension.invokeMethodLogin(object);
    }
    public boolean methodIsAnnotAuthTest() {

        String[] methodsStack = authenticatedTestExtension.getMethods();
        List<String> methodsAuthenticatedTest = authenticatedTestExtension.getMethodsAuthenticatedTest(object);

        if(methodsAuthenticatedTest.contains(methodsStack[3]))
            return true;

        return false;
    }

    public boolean verifyMethodLogin() {
        return authenticateExtension.methodLoginIsIstantiated(object);
    }

    public String convertTypeHeaders(Header header) throws NotImplementedRequestException {

        if(header.getTypeHeader().equals(TypeHeader.BEARER))
            return String.format("%s %s",header.getTypeHeader().name(), header.getValues()[0]);

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
