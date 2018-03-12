/**
 * берет данные из  QueueGPS  помещает в базу данных (очередь QueueDB или Лог) в формате json
 * периодичность ~0,5 минутa
 */
package edu.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dto.PointDTO;
import edu.tracker.storage.QueueGPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataSend {

    private static final Logger log = LoggerFactory.getLogger(DataSend.class);

    @Value("${data.send.packet.size}")
    private int packet;

    @Autowired
    private QueueGPS queueGPS;

    @Scheduled(cron = "${data.send.cron}")
    private void sendToDB() {
        // как выбрать всё из очереди?
        PointDTO localpoint = null;
        for (int i=1;i<=packet;++i){
            try {
                localpoint = queueGPS.take(); // выборка из очереди
            } catch (InterruptedException e) {
//                e.printStackTrace();
                break;
            }
            try {
                log.info(localpoint.toJson() + "  (" + i + ")"); // типа сохранение в БД
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
