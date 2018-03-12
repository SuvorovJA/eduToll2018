/**
 * берет данные из  QueueGPS  помещает в базу данных (очередь QueueDB или Лог) в формате json
 * периодичность ~1 минутa
 */
package edu.tracker.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataSend {
    @Scheduled(cron = "${data.send.cron}")
    private void sendToDB() {
        System.out.println("DataSend.sendToDB()");
    }

}
