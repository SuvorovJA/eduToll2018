package edu.server.context;

import edu.server.controllers.ReceiveController;
import edu.server.controllers.ShowController;
import edu.server.crud.Crud;
import edu.server.services.OpenFileForWrite;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@SpringBootApplication
@ComponentScan({"services", "controllers", "context"})
@EnableJpaRepositories(basePackages = "edu.server.repository.repo")
@EntityScan(basePackageClasses = edu.server.repository.PointDTOEntity.class)
public class InjectionContext {

    @Bean
    public ReceiveController receiveController() {
        return new ReceiveController();
    }

    @Bean
    public ShowController showController(){
        return  new ShowController();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public OpenFileForWrite openFileForWrite() {
        return new OpenFileForWrite();
    }

    // jpa+h2
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Crud crud(){
        return new Crud();
    }

//    @Bean //этот бин создаётся спрингбутом автоматически по @EnableJpaRepositories
//    public PointDTORepository pointDTORepository(){
//        return new PointDTORepository();
//    }
}
