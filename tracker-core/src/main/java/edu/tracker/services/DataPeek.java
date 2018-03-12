/**
 * берет данные из GpsService  помещает в QueueGPS
 * периодичность ~1 sec
 */
package edu.tracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dto.PointDTO;
import edu.tracker.context.InjectionContext;
import edu.tracker.storage.QueueGPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class DataPeek {

    @Autowired
    private GpsService gpsService;

    @Autowired
    private QueueGPS queueGPS;

    @Autowired
    private PointDTO pointDTO;

    @Scheduled(cron = "${data.peek.cron}")
    private void fetchGps() {
        String className = this.getClass().getName().toString();
        // получить json от GpsService
        String localstring = gpsService.getNext();
        System.out.println(className + ": fetchGps() fetched: " + localstring);
        // переделать json в объект PointDTO
        ObjectMapper mapper = new ObjectMapper();
        PointDTO localpoint = null;
        try {
            localpoint = mapper.readValue(localstring, PointDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // положить объект в Очередь QueueGPS
        try {
            queueGPS.put(localpoint);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
