/**
 * берет данные из GpsService  помещает в QueueGPS
 * периодичность ~1 sec
 */
package edu.tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DataPeek {


    @Scheduled(cron = "${data.peek.cron}")
    private void fetchGps() {
        System.out.println("DataPeek.fetchGps()");
    }


}
