package org.api.mocktests;

import org.api.mocktests.annotations.AuthenticatedTest;
import org.api.mocktests.annotations.AutoConfigureRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {org.api.mocktests.MockTestsLibrary.class})
@EnableAutoConfiguration
@AutoConfigureMockMvc
@AutoConfigureRequest(header = {"Authorization", "Bearer fghjkkkkkkkkkkkkkkkkkkkkkkkkkk"})
public class MockTests {

    @Autowired
    private MockMvc mockMvc;

    //private MockTest requestUtils = new MockTest(this);

    @Test
    @AuthenticatedTest
    public void test01() throws Exception {
        //mockMvc.perform(new Request().operation(Method.POST).endpoint("/api").execute()).andExpect(status().is2xxSuccessful());
    }

}
