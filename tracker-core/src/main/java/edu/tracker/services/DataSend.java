/*
 * берет данные из  QueueGPS  помещает в базу данных (лог-БД, очередь QueueDB или Лог) в формате json
 * периодичность ~0,5 минутa
 */
package edu.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dto.PointDTO;
import edu.tracker.controllers.SendController;
import edu.tracker.crud.Crud;
import edu.tracker.storage.QueueGPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataSend {

    private static final Logger log = LoggerFactory.getLogger(DataSend.class);

    private QueueGPS queueGPS;

    private SendController sendController;

    @Autowired
    private Crud crud;

    public DataSend(@Autowired QueueGPS queueGPS, @Autowired SendController sendController) {
        this.queueGPS = queueGPS;
        this.sendController = sendController;
    }

    @Scheduled(cron = "${data.send.cron}")
    public void sendToDB() throws InterruptedException, JsonProcessingException {
        int packet = queueGPS.getSize();
        if (packet == 0) {
            log.warn("очередь пуста");
            return;
        }
        PointDTO localpoint; // = null;
        for (int i = 1; i <= packet; ++i) {
            localpoint = queueGPS.take(); // выборка из очереди
            // в локальный лог
            // log.info(localpoint.toJson() + "  (" + i + ")");
            // в ДБ server-core, в возврате эхо от приёмника
            sendController.setPoint(localpoint);
            // в свою лог-ДБ, ну и в консольный лог до кучи, нам не жалко
            log.info(crud.create(localpoint).toString());
        }
    }

}
