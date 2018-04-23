package edu.tracker.storage;

import edu.dto.PointDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
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

    private static final Logger log = LoggerFactory.getLogger(QueueGPS.class);

    private BlockingDeque<PointDTO> queue = new LinkedBlockingDeque<>(100*5);

    public PointDTO take() throws InterruptedException {
        return queue.take();
    }

    @Async
    public void put(PointDTO p) throws InterruptedException {
        log.info("@Async/put()");
        queue.put(p);
    }

    public int getSize(){
        return queue.size();
    }

    @Scheduled(cron = "${queue.monitor.cron}")
    public  void monitoring(){
        log.info("size of QueueGPS = " + queue.size());
    }
}
