package org.api.mocktests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.annotations.Authenticate;
import org.api.mocktests.annotations.AuthenticatedTest;
import org.api.mocktests.models.Operation;
import org.api.mocktests.models.Request;
import org.api.mocktests.utils.MockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class MockTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private MockTest mockTest = new MockTest(mockMvc, this);

    @Authenticate
    private Request requestLogin = new Request().operation(Operation.POST).endpoint("URL").contentType("application/json");

    @Test
    @AuthenticatedTest
    public void test01() throws Exception {
        mockTest.performTest(new Request().operation(Operation.POST).endpoint("URL").contentType("application/json").body(new Object()));
    }

    @Test
    @AuthenticatedTest
    public void test02() throws Exception {
        mockTest.performTest(new Request().operation(Operation.POST).endpoint("URL").contentType("application/json").body(new Object()));
    }

    @Test
    @AuthenticatedTest
    public void test03() throws Exception {
        mockTest.performTest(new Request().operation(Operation.POST).endpoint("URL").contentType("application/json").body(new Object()));
    }
}
