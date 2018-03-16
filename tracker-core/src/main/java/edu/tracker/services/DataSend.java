/*
 * берет данные из  QueueGPS  помещает в базу данных (очередь QueueDB или Лог) в формате json
 * периодичность ~0,5 минутa
 */
package edu.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dto.PointDTO;
import edu.tracker.controllers.SendController;
import edu.tracker.storage.QueueGPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataSend {

    private static final Logger log = LoggerFactory.getLogger(DataSend.class);

    @Autowired
    private QueueGPS queueGPS;

    @Autowired
    private SendController sendController;

    @Scheduled(cron = "${data.send.cron}")
    private void sendToDB() {
        int packet = queueGPS.getSize();
        if (packet == 0) {
            log.warn("очередь пуста");
            return;
        }
        PointDTO localpoint; // = null;
        for (int i = 1; i <= packet; ++i) {
            try {
                localpoint = queueGPS.take(); // выборка из очереди
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                break;
            }
            // в локальный лог
            try {
                log.info(localpoint.toJson() + "  (" + i + ")");
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                break;
            }
            // в ДБ
            sendController.setPoint(localpoint);
        }
    }

}
