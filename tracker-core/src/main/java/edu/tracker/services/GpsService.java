/*
 * по запросу от DataPeek должен выдать координатную точку
 */
package edu.tracker.services;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

@Service
public class GpsService {
    
    private static final Logger log = LoggerFactory.getLogger(GpsService.class);
    
    @Value("${gpx.source}")
    private String fileName;

    private List<WayPoint> wpl;
    private ListIterator<WayPoint> itWpl;

    @PostConstruct
    public void init() {
        log.info("gpx.source = " + fileName);
        String filePath = this.getClass().getResource("/" + fileName).getPath().toString();
        log.info("filePath = " + filePath);
        // читаем файл в List // только трэк 0 и только сегмент 0
        final boolean lenient = true;
        GPX gpx; // = null;
        try {
            gpx = GPX.read(filePath, lenient);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        wpl = gpx.getTracks().get(0).getSegments().get(0).getPoints();
        log.info("Считано " + wpl.size() + " точек.");
        // получить итератор на начало списка
        itWpl = wpl.listIterator();
    }

    // результат без использования com.fasterxml.jackson.
    // id автомобиля всегда один и тот же.
    public String getNext() {
        if(wpl.isEmpty()) {
            log.error("нет импортированных данных из " + fileName);
            return "{}";
        }
        WayPoint localpoint; // = null;
        if (!itWpl.hasNext()) {
            itWpl = wpl.listIterator(0); // начинаем с начала;
        }
        localpoint = itWpl.next();
        return "{\"lat\":" + localpoint.getLongitude() + "," +
                "\"lon\":" + localpoint.getLatitude() + "," +
                "\"autoId\": \"E555EM70\"," +
                "\"time\":" + localpoint.getTime().get().toInstant().toEpochMilli() +
                "}";
    }
}
