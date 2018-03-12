/**
 * создание объектов служб, включение шедулера, подключение файла пропертисов
 */

package edu.tracker.context;

import edu.tracker.services.DataPeek;
import edu.tracker.services.DataSend;
import edu.tracker.services.GpsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@PropertySource("classpath:/properties.ini")
public class InjectionContext {

    @Bean
    public DataPeek peekService() {
        return new DataPeek();
    }

    @Bean
    public DataSend sendService() {
        return new DataSend();
    }

    @Bean
    public GpsService gpsService() {
        return new GpsService();
    }


}
