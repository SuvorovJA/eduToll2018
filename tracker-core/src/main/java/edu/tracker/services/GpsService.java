/**
 * по запросу от DataPeek должен выдать координатную точку
 */
package edu.tracker.services;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

@Service
public class GpsService {

    @Value("${gpx.source}")
    private String fileName;

    private List<WayPoint> wpl;
    private ListIterator<WayPoint> itWpl;

    @PostConstruct
    public void init() {
        String className = this.getClass().getName().toString();
        // TODO replace sout to logging
        System.out.println(className + ": gpx.source = " + fileName);
        String filePath = this.getClass().getResource("/" + fileName).getPath().toString();
        System.out.println(className + ": filePath = " + filePath);
        // читаем файл в List // только трэк 0 и только сегмент 0
        final boolean lenient = true;
        GPX gpx = null;
        try {
            gpx = GPX.read(filePath, lenient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wpl = gpx.getTracks().get(0).getSegments().get(0).getPoints();
        System.out.println(className + ": Считано " + wpl.size() + " точек.");
        // получить итератор на начало списка
        itWpl = wpl.listIterator();
    }

    // результат без использования com.fasterxml.jackson.
    // id автомобиля всегда один и тот же.
    public String getNext() {
        // TODO нет проверки на пустой список
        WayPoint localpoint = null;
        if (!itWpl.hasNext()) {
            itWpl = wpl.listIterator(0); // начинаем с начала;
        }
        localpoint = itWpl.next();
        String result = "{\"lat\":" + localpoint.getLongitude() + "," +
                "\"lon\":" + localpoint.getLatitude() + "," +
                "\"autoId\": \"E555EM70\"," +
                "\"time\":" + localpoint.getTime().get().toInstant().toEpochMilli() +
                "}";
        return result;
    }
}
