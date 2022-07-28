package org.api.mocktests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.mocktests.annotations.Authenticate;
import org.api.mocktests.annotations.AuthenticatedTest;
import org.api.mocktests.models.Operation;
import org.api.mocktests.models.Request;
import org.api.mocktests.utils.RequestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {org.api.mocktests.MockTestsLibrary.class})
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class MockTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private RequestUtils requestUtils = new RequestUtils(this);

    //@Authenticate
    //private final Request requestLogin = new Request().operation(Operation.POST).endpoint("/api").contentType("application/json");

    @Authenticate
    ResultActions login() throws Exception {
        return mockMvc.perform(new Request(requestUtils)
                .operation(Operation.POST)
                .endpoint("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body("login")
                .compileRequest());
    }
    @Test
    //@AuthenticatedTest
    public void test01() throws Exception {
        //mockMvc.perform(post("/api")).andExpect(status().is4xxClientError());
        mockMvc.perform(new Request(requestUtils)
                .operation(Operation.POST)
                .endpoint("/api/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body("objeto1")
                .compileRequest());
    }

    @Test
    @AuthenticatedTest
    public void test02() throws Exception {
        mockMvc.perform(new Request(requestUtils)
                .operation(Operation.POST)
                .endpoint("/api/2")
                .contentType(MediaType.APPLICATION_JSON)
                .body("objeto2")
                .compileRequest());
    }

    @Test
    @AuthenticatedTest
    public void test03() throws Exception {
        mockMvc.perform(new Request(requestUtils)
                .operation(Operation.POST)
                .endpoint("/api/3")
                .contentType(MediaType.APPLICATION_JSON)
                .body("objeto3")
                .compileRequest());
    }
}
