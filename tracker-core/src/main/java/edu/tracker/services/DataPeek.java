/*
 * берет данные из GpsService  помещает в QueueGPS
 * периодичность ~1 sec
 */
package edu.tracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dto.PointDTO;
import edu.tracker.storage.QueueGPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataPeek {

    private static final Logger log = LoggerFactory.getLogger(DataPeek.class);

    @Autowired
    private GpsService gpsService;

    @Autowired
    private QueueGPS queueGPS;

    @Autowired
    private PointDTO pointDTO;

    @Scheduled(cron = "${data.peek.cron}")
    private void fetchGps() {
        // получить json от GpsService
        String localstring = gpsService.getNext();
        log.info("fetchGps() " + localstring);
        // переделать json в объект PointDTO
        ObjectMapper mapper = new ObjectMapper();
        PointDTO localpoint = null;
        try {
            localpoint = mapper.readValue(localstring, PointDTO.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        // положить объект в Очередь QueueGPS
        try {
            queueGPS.put(localpoint);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }


}
