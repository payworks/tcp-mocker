package io.payworks.labs.tcpmocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.payworks.labs.tcpmocker.controller.model.ResponseDelayModel;
import io.payworks.labs.tcpmocker.properties.TcpMockerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DelayResponseController.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DelayResponseControllerTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private TcpMockerProperties properties;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void getDelayCollectsValueFromProperties() throws Exception {
        final var givenDuration = Duration.ofSeconds(10L);
        given(properties.getResponseDelay()).willReturn(givenDuration);

        mvc.perform(
                get("/responsedelay")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration", is("PT10S")));
    }

    @Test
    public void setDelayPopulatesValueToPropertiesAndReturnItsValue() throws Exception {
        final var givenDuration = Duration.ofSeconds(20L);
        final var givenResponseDelay = new ResponseDelayModel(givenDuration);
        given(properties.getResponseDelay()).willReturn(givenDuration);

        mvc.perform(
                post("/responsedelay")
                        .content(mapper.writeValueAsString(givenResponseDelay))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration", is("PT20S")));

        then(properties).should().setResponseDelay(givenDuration);
        then(properties).should().getResponseDelay();
    }

    @Test
    public void setDelayDoesNotAcceptNegativeValues() throws Exception {
        final var givenDuration = Duration.ofSeconds(-1L);
        final var givenResponseDelay = new ResponseDelayModel(givenDuration);
        given(properties.getResponseDelay()).willReturn(givenDuration);

        mvc.perform(
                post("/responsedelay")
                        .content(mapper.writeValueAsString(givenResponseDelay))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        then(properties).shouldHaveNoInteractions();
    }
}