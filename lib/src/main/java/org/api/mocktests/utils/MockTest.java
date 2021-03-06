package org.api.mocktests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.exceptions.InvalidRequestException;
import org.api.mocktests.exceptions.NotImplementedRequestException;
import org.api.mocktests.exceptions.UnauthorizedRequestException;
import org.api.mocktests.extensions.AuthenticateExtension;
import org.api.mocktests.extensions.AuthenticatedTestExtension;
import org.api.mocktests.models.Header;
import org.api.mocktests.models.Operation;
import org.api.mocktests.models.Request;
import org.api.mocktests.models.TypeHeader;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@EnableAutoConfiguration
@AutoConfigureMockMvc
public final class MockTest {

    @Autowired
    @InjectMocks
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Object object;

    private final AuthenticateExtension authenticateExtension = new AuthenticateExtension();
    private final AuthenticatedTestExtension authenticatedTestExtension = new AuthenticatedTestExtension(object);

    public MockTest(Object object) {
        super();
        MockTest.object = object;
    }

    public void performTest(Request request) throws Exception {

        verifyRequest(request);
        if(request.getHeader() == null) {
            if(authenticateExtension.fieldLoginIsIstantiated(object)) {

                String[] methodsStack = authenticatedTestExtension.getMethods();
                List<String> methodsAuthenticatedTest = authenticatedTestExtension.getMethodsAuthenticatedTest(object);

                if(methodsAuthenticatedTest.contains(methodsStack[3])) {

                    Request requestLogin = authenticateExtension.getFieldLogin(object);
                    ResultActions resultLogin = invokeLogin(requestLogin);
                    if (resultLogin.andReturn().getResponse().getStatus() >= 200 && resultLogin.andReturn().getResponse().getStatus() < 300) {
                        String token = resultLogin.andReturn().getResponse().getContentAsString();
                        String[] values = token.split(" ");
                        mockMvc.perform(convertOperation(request.getOperation(), request.getEndpoint(), request.getParams())
                                .header(values[0], values[1], values[2])
                                .contentType(request.getContentType())
                                .content(objectMapper.writeValueAsString(request.getBody())));
                    } else
                        throw new UnauthorizedRequestException("Login failed.");
                }
            }
            mockMvc.perform(convertOperation(request.getOperation(), request.getEndpoint(), request.getParams())
                    .contentType(request.getContentType())
                    .content(objectMapper.writeValueAsString(request.getBody())));
        }
        else
            mockMvc.perform(convertOperation(request.getOperation(), request.getEndpoint(), request.getParams())
                .header(request.getHeader().getName(), convertTypeHeaders(request.getHeader()))
                .contentType(request.getContentType())
                .content(objectMapper.writeValueAsString(request.getBody())));
    }

    private String convertTypeHeaders(Header header) throws NotImplementedRequestException {

        if(header.getTypeHeader().equals(TypeHeader.BEARER))
            return String.format("%s %s",header.getTypeHeader().name(), header.getValues()[0]);

        throw new NotImplementedRequestException(String.format("Type header %s not implemented!",header.getName()));
    }

    private ResultActions invokeLogin(Request request) throws Exception {

        if(mockMvc != null) {
            System.out.println(mockMvc.toString());
        }
        else
            System.out.println("mockMvc NULL");

        if(objectMapper != null) {
            System.out.println(objectMapper.toString());
        }
        else
            System.out.println("objectMapper NULL");

        System.out.println("OPERATION: "+ request.getOperation().name());
        System.out.println("ENDPOINT: "+request.getEndpoint());
        System.out.println("PARAMS: "+ Arrays.toString(request.getParams()));
        System.out.println("CONTENT TYPE: "+request.getContentType());
        System.out.println("BODY: "+request.getBody().toString()+"\n");

        return mockMvc.perform(convertOperation(request.getOperation(), request.getEndpoint(), request.getParams())
                .contentType(request.getContentType())
                .content(objectMapper.writeValueAsString(request.getBody())));
    }
    private MockHttpServletRequestBuilder convertOperation(Operation operation, String endpoint, Object[] params) {

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

    private void verifyRequest(Request request) throws InvalidRequestException {
        if(request.getOperation() == null)
            throw new InvalidRequestException();
        if(request.getEndpoint() == null)
            throw new InvalidRequestException();
    }
}
