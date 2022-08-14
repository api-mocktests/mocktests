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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {org.api.mocktests.MockTestsLibrary.class})
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class MockTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private MockTest mockTest = new MockTest(this);

    @Authenticate
    private final Request requestLogin = new Request().operation(Operation.POST).endpoint("/api").contentType("application/json");

    @Test
    //@AuthenticatedTest
    public void test01() throws Exception {
        //mockMvc.perform(post("/api")).andExpect(status().is4xxClientError());
        mockTest.performTest(new Request().operation(Operation.POST).endpoint("/api/1").contentType("application/json").body("objeto1"));
    }

    @Test
    @AuthenticatedTest
    public void test02() throws Exception {
        mockTest.performTest(new Request().operation(Operation.POST).endpoint("/api/2").contentType("application/json").body("objeto2"));
    }

    @Test
    @AuthenticatedTest
    public void test03() throws Exception {
        mockTest.performTest(new Request().operation(Operation.POST).endpoint("/api/3").contentType("application/json").body("objeto3"));
    }
}
