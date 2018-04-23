/*
 * по запросу от DataPeek должен выдать координатную точку
 */
package edu.tracker.services;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

@Service
public class GpsService {

    private static final Logger log = LoggerFactory.getLogger(GpsService.class);

    @Value("${gpx.sources.list}")
    private String[] fileNames;

    @Value("${gpx.autoids.list}")
    private String[] autoIds;

    @Value("${num.of.machines}")
    private int num_of_threads;

    private List<WayPoint>[] wpls;  // array of list
    private ListIterator<WayPoint>[] itWpls; // array of list

    @PostConstruct
    public void init() throws IOException {
        wpls = new List[num_of_threads];
        itWpls = new ListIterator[num_of_threads];
        // вычитаваем точки для всех машин, привет памяти
        for (int i = 0; i < num_of_threads; i++) {
            String fileName = fileNames[i];
            log.info("gpx.source[" + i + "]= " + fileName); // Thread.currentThread().getName() итак в лог выводится
            // читаем файл в List // только трэк 0 и только сегмент 0
            final boolean lenient = true;
            GPX gpx = null;
            gpx = GPX.read(this.getClass().getResourceAsStream("/" + fileName));
            wpls[i] = gpx.getTracks().get(0).getSegments().get(0).getPoints();
            log.info("Считано " + wpls[i].size() + " точек для машины " + autoIds[i]);
            // получить итераторы на начала списков
            itWpls[i] = wpls[i].listIterator();
        }
    }

    // результат без использования com.fasterxml.jackson.
    // параметром - индекс машины из ("${gpx.sources.list}")
    // возвратом должен быть Future
    @Async
    public Future<String> getNext(int i) {
        if (wpls[i].isEmpty()) {
            String fileName = fileNames[i];
            log.error("нет импортированных данных из " + fileName);
            return new AsyncResult<String>("{}");
        }
        WayPoint localpoint; // = null;
        if (!itWpls[i].hasNext()) {
            itWpls[i] = wpls[i].listIterator(0); // начинаем с начала;
        }
        localpoint = itWpls[i].next();
        log.info("@Async/getNext(" + i + ")");
        return new AsyncResult<String>("{\"lat\":" + localpoint.getLongitude() + "," +
                "\"lon\":" + localpoint.getLatitude() + "," +
                "\"autoId\":\"" + autoIds[i] + "\"," +
                "\"time\":" + localpoint.getTime().get().toInstant().toEpochMilli() +
                "}");
    }


    // TODO fix id [0] for tests
    // only for tests
    public int getSize() {
        return wpls[0].size();
    }

    // only for tests
    public void setFileName(String fileName) {
        this.fileNames[0] = fileName;
    }
}
