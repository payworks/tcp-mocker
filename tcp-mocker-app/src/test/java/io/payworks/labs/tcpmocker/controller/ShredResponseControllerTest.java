package io.payworks.labs.tcpmocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.payworks.labs.tcpmocker.controller.resources.ResponseDelayResource;
import io.payworks.labs.tcpmocker.controller.resources.ResponseShreddingResource;
import io.payworks.labs.tcpmocker.properties.TcpMockerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShredResponseController.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShredResponseControllerTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private TcpMockerProperties properties;

    @Autowired
    private ObjectMapper mapper;

    @DataProvider
    Object[][] givenShredResponses() {
        return new Object[][] {
                {true},
                {false}
        };
    }

    @Test(dataProvider = "givenShredResponses")
    public void getResponseShreddingCollectsValueFromProperties(final boolean givenShredResponses) throws Exception {
        given(properties.isShredResponses()).willReturn(givenShredResponses);

        mvc.perform(
                get("/responseshredding")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled", is(givenShredResponses)));
    }

    @Test
    public void setResponseShreddingPopulatesValueToPropertiesAndReturnsItsValue() throws Exception {
        final var givenShredResponses = true;
        final var givenResponseShredding = new ResponseShreddingResource(givenShredResponses);

        given(properties.isShredResponses()).willReturn(givenShredResponses);

        mvc.perform(
                post("/responseshredding")
                        .content(mapper.writeValueAsString(givenResponseShredding))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled", is(true)));

        then(properties).should().setShredResponses(givenShredResponses);
        then(properties).should().isShredResponses();
    }
}