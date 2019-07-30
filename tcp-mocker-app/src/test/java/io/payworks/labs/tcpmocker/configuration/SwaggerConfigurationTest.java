package io.payworks.labs.tcpmocker.configuration;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SwaggerConfigurationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void anonymousUserCanAccessApiDocs() throws Exception {
        mvc.perform(get("/v2/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    public void anonymousUserCanAccessSwaggerUi() throws Exception {
        mvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiDocsHaveExpectedDefinitions() throws Exception {
        mvc.perform(get("/v2/api-docs"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.info.title").value(Matchers.equalToIgnoringCase("Payworks TCP Mocker REST API")))
                .andExpect(jsonPath("$.paths./recordings").exists())
                .andExpect(jsonPath("$.paths./recordings/last").exists());
    }
}
