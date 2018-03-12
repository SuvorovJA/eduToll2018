// TEST-DRIVE-PROJECT. no Spring integration
package edu.gpxreader;
/**
 *
 * метод getNext() неограниченно возвращает Json строку из GPX файла взятого
 * с ресурса https://www.gpslib.ru/tracks/download/11060.gpx
 * вычитывается только нулевой трэк, нулевой сегмент
 *
 */

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class DeviceEmulator {

    private List<WayPoint> wpl;
    private ListIterator<WayPoint> itWpl;

    public DeviceEmulator(String file) {
        String filePath = this.getClass().getResource(file).getPath().toString();
        final boolean lenient = true;
        GPX gpx = null;
        try {
            gpx = GPX.read(filePath, lenient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wpl = gpx.getTracks().get(0).getSegments().get(0).getPoints();
        // TODO replace to logging
        System.out.println(this.getClass().getName().toString() + ": Считано " + wpl.size() + " точек.");
        itWpl = wpl.listIterator(); // получить итератор на начало списка
    }

    // результат без использования com.fasterxml.jackson.
    public String getNext() {
        // TODO нет проверки на пустой список
        WayPoint localpoint = null;
        if (!itWpl.hasNext()) {
            itWpl = wpl.listIterator(0); // начинаем с начала; //  надо проверить - OK, in Main2
        }
        localpoint = itWpl.next();
        String result = "{\"lat\":" + localpoint.getLongitude() + "," +
                "\"lon\":" + localpoint.getLatitude() + "," +
                "\"time\":" +
                localpoint.getTime().get().toInstant().toEpochMilli() +
                "}";
        return result;
    }
}
