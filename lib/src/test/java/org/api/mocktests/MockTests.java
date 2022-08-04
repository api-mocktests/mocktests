package org.api.mocktests;

import org.api.mocktests.annotations.AuthenticatedTest;
import org.api.mocktests.annotations.AutoConfigureRequest;
import org.api.mocktests.models.Operation;
import org.api.mocktests.models.Request;
import org.api.mocktests.utils.RequestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {org.api.mocktests.MockTestsLibrary.class})
@EnableAutoConfiguration
@AutoConfigureMockMvc
@AutoConfigureRequest(header = {"Authorization", "Bearer fghjkkkkkkkkkkkkkkkkkkkkkkkkkk"})
public class MockTests {

    @Autowired
    private MockMvc mockMvc;

    //private RequestUtils requestUtils = new RequestUtils(this);

    @Test
    @AuthenticatedTest
    public void test01() throws Exception {
        mockMvc.perform(new Request().operation(Operation.POST).endpoint("/api").execute()).andExpect(status().is2xxSuccessful());
    }

}
