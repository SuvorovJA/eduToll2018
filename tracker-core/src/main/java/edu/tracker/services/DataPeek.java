/**
 * берет данные из GpsService  помещает в QueueGPS
 * периодичность ~1 sec
 */
package edu.tracker.services;

import edu.tracker.context.InjectionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DataPeek {

    @Autowired
    private GpsService gpsService;


    @Scheduled(cron = "${data.peek.cron}")
    private void fetchGps() {
        String className = this.getClass().getName().toString();
        String localstring = gpsService.getNext();
        System.out.println(className + ": fetchGps() fetched: " + localstring);
    }


}
