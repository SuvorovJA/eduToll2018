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

    private GpsService gpsService; //wired
    private QueueGPS queueGPS; // wired
    private PointDTO pointDTO; // local

    // конструктор только для тестов, без тестов достаточно @Autowired QueueGPS queueGPS; @Autowired GpsService gpsServic;
    public DataPeek(@Autowired QueueGPS queueGPS, @Autowired GpsService gpsService){
        this.pointDTO = null;
        this.queueGPS = queueGPS;
        this.gpsService = gpsService;
    }

    @Scheduled(cron = "${data.peek.cron}")
    public void fetchGps() {
        // получить json от GpsService
        String localstring = gpsService.getNext();
        log.info("fetchGps() " + localstring);
        // переделать json в объект PointDTO
        ObjectMapper mapper = new ObjectMapper();
        try {
            pointDTO = mapper.readValue(localstring, PointDTO.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        // положить объект в Очередь QueueGPS
        try {
            queueGPS.put(pointDTO);
        } catch (NullPointerException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }


}
