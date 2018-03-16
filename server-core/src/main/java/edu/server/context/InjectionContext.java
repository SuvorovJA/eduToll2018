package edu.server.context;

import edu.server.controllers.ReceiveController;
import edu.server.services.OpenFileForWrite;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@SpringBootApplication
@ComponentScan({"services", "controllers", "context"})
public class InjectionContext {

    @Bean
    public ReceiveController receiveController() {
        return new ReceiveController();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public OpenFileForWrite openFileForWrite() {
        return new OpenFileForWrite();
    }
}
