package edu.tracker.storage;

import edu.dto.PointDTO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Created by jdev on 26.03.2017.
 * Copy|Mod by sua on 12-03-2018
 */

@Service
public class QueueGPS {

    //    private static final Logger log = LoggerFactory.getLogger(QueueGPS.class);
    private BlockingDeque<PointDTO> queue = new LinkedBlockingDeque<>(1000);

    public PointDTO take() throws InterruptedException {
        return queue.take();
    }

    public void put(PointDTO p) throws InterruptedException {
        queue.put(p);
    }

    @Scheduled(cron = "${queue.monitor.cron}")
    public  void monitoring(){
        String className = this.getClass().getName().toString();
        System.out.println(className + ": size of QueueGPS = " + queue.size());
    }
}
