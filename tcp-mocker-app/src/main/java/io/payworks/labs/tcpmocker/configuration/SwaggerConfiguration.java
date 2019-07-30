package io.payworks.labs.tcpmocker.configuration;

import io.payworks.labs.tcpmocker.MockServerApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api(final ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(MockServerApp.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(URI.class, String.class)
                .genericModelSubstitutes(Optional.class, ResponseEntity.class)
                .apiInfo(new ApiInfo(
                        "Payworks TCP Mocker REST API",
                        "Use the 'TCP Mocker' REST API to retrieve requests and corresponding replies with timestamps",
                        "2019.7",
                        null,
                        new Contact("Enrique Alonso-Martin", null, "enrique.alonso-martin@payworks.com"),
                        "© 2019 Payworks",
                        null,
                        Collections.emptyList()));
    }
}
