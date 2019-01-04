package io.payworks.labs.tcpmocker.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static java.lang.String.format;

@Configuration
public class AliasWebConfig implements WebMvcConfigurer {

    private WebEndpointProperties actuatorWebProperties;

    public AliasWebConfig(@Autowired(required = false) WebEndpointProperties actuatorWebProperties) {
        this.actuatorWebProperties = actuatorWebProperties;
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        if (actuatorWebProperties != null) {
            String actuatorPrefix = actuatorWebProperties.getBasePath();

            registry.addViewController("/health").setViewName(format("forward:%s/health", actuatorPrefix));
            registry.addViewController("/info").setViewName(format("forward:%s/info", actuatorPrefix));
            registry.addViewController("/metrics").setViewName(format("forward:%s/metrics", actuatorPrefix));
        }
    }
}
