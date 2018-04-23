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
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DataPeek {

    private static final Logger log = LoggerFactory.getLogger(DataPeek.class);

    @Value("${num.of.machines}")
    private int num_of_threads;

    private GpsService gpsService; //wired
    private QueueGPS queueGPS; // wired
    private PointDTO pointDTO; // local

    private Future<String>[] localfuturestrings;
//    private Future<Integer>[] localfuturefakeints;
    private String[] localjsonresults;

    // конструктор только для тестов, без тестов достаточно @Autowired QueueGPS queueGPS; @Autowired GpsService gpsServic;
    public DataPeek(@Autowired QueueGPS queueGPS, @Autowired GpsService gpsService) {
        this.pointDTO = null;
        this.queueGPS = queueGPS;
        this.gpsService = gpsService;

    }

    @PostConstruct
    public void init() throws IOException {
        localfuturestrings = new Future[num_of_threads];
        localjsonresults = new String[num_of_threads];
//        localfuturefakeints = new Future[num_of_threads];
    }

    @Scheduled(cron = "${data.peek.cron}")
    public void fetchGps() throws ExecutionException, InterruptedException {
        // получить json от GpsService запустив пучёк потоков
        for (int i = 0; i < num_of_threads; i++) {
            localfuturestrings[i] = gpsService.getNext(i);
        }
        // дождаться результатов. здесь тупим в одном потоке
        for (int i = 0; i < num_of_threads; i++) {
            localjsonresults[i] = ""; // на всякий случай
            localjsonresults[i] = localfuturestrings[i].get(); //get() is blocking
            log.info("fetchGps() " + localjsonresults[i]);
        }
        // скинуть все результаты в очередь QueueGPS снова запустив пучёк потоков
        // очередь блокирующая поэтому... что ?
        for (int i = 0; i < num_of_threads; i++) {
//            putToQueue(json2point(localjsonresults[i])); //Async ? no work
//            PointDTO pointDTO = json2point(localjsonresults[i]);
//            putToQueue(pointDTO); //Async ? no work
//            localfuturefakeints[i] = this.putToQueue(json2point(localjsonresults[i])); //Async ? no work
            queueGPS.put(json2point(localjsonresults[i])); // Async in put() ? THAT WORK !!!
        }
        // здесь наверное надо дождаться исполнения всех putToQueue() ? как ?
        // наверное само дождется, это-ж спринг
    }

    private PointDTO json2point(String localstring) {
        // переделать json в объект PointDTO
        ObjectMapper mapper = new ObjectMapper();
        try {
            pointDTO = mapper.readValue(localstring, PointDTO.class);
        } catch (IOException e) {
            log.error(e.getMessage()); // suppress exceptions
            return new PointDTO();
        }
        return pointDTO;
    }

////    @Async
////    private void putToQueue(PointDTO pointDTO) { // Async no work with void
//    @Async
//    public Future<Integer> putToQueue(PointDTO pointDTO) {
//        // положить объект в Очередь QueueGPS
//        try {
//            log.info("@Async/putToQueue()");
//            queueGPS.put(pointDTO);
//        } catch (NullPointerException | InterruptedException e) {
//            log.error(e.getMessage()); // suppress exceptions
//            return new AsyncResult<>(-1);
//        }
//        return new AsyncResult<>(1);
//    }
}
