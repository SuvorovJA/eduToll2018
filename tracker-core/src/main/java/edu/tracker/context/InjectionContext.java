/*
 * создание объектов служб, включение шедулера, подключение файла пропертисов
 */

package edu.tracker.context;

import edu.dto.PointDTO;
import edu.tracker.controllers.SendController;
import edu.tracker.services.DataPeek;
import edu.tracker.services.DataSend;
import edu.tracker.services.GpsService;
import edu.tracker.storage.QueueGPS;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({"context", "services", "controllers", "storage"})
@Configuration
@EnableScheduling
@PropertySource("classpath:/properties.ini")
public class InjectionContext {

    @Bean
    public SendController sendController() {
        return new SendController();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public GpsService gpsService() {
        return new GpsService();
    }

    @Bean
    public QueueGPS queueGPS() {
        return new QueueGPS();
    }

    @Bean  // TODO а зачем нужен этот бин?
    public PointDTO pointDTO() {
        return new PointDTO();
    }

    @Bean
    public DataSend sendService() {
        return new DataSend(queueGPS(), sendController());
    }

    @Bean
    public DataPeek peekService() {
        return new DataPeek(queueGPS(), gpsService());
    }

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(20);
        return scheduler;
    }

}
