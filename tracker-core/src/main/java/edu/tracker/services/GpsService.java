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
import java.net.URISyntaxException;
import java.nio.file.Paths;
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
        // *******************************************************************************
        // это
        //      String filePath = this.getClass().getResource("/"+fileName).getPath().toString();
        // при размещении файла в src/main/resources (при сборке копируется в out/production/resources/)
        // на windows даёт сбой
        // ....InvalidPathException: Illegal char <:> at index 2: /C:/Users....
        // ________________лишний лидирующий слэш_________________^_____________
        // если править к ....getResource(fileName)..., даёт NPE и на windows и на Linux
        // так как этот слэш ("/"+fileName) это не Тот слэш
        // решение исполненное на windows:
        //      ClassLoader cl = getClass().getClassLoader();
        //      String filePath = cl.getResource(fileName).getPath();
        // всёравно даёт ...InvalidPathException: Illegal char <:> at index 2: /C:/Users/...
        // https://stackoverflow.com/questions/9834776/java-nio-file-path-issue
        // You need to convert the found resource to URI. It works on all platforms and protects
        // you from possible errors with paths. You must not worry about how full path looks like,
        // whether it starts with '\' or other symbols. If you think about such details - you do
        // something wrong.
        // решение работает на обеих ОС:
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String filePath = null;
        try {
            filePath = Paths.get(cl.getResource(fileName).toURI()).toString();
        } catch (NullPointerException | URISyntaxException e) {
            log.error(e.getMessage());
        }
        // filePath = C:\Users\lsua\IdeaProjects\eduToll2018\tracker-core\out\production\resources\11060.gpx
        // filePath = /home/lsua/DEV/DO-TUSUR/proJava/eduToll2018/tracker-core/out/production/resources/11060.gpx
        // *******************************************************************************
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
        if (wpl.isEmpty()) {
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
                "\"autoId\":\"E555EM70\"," +
                "\"time\":" + localpoint.getTime().get().toInstant().toEpochMilli() +
                "}";
    }

    // only for tests
    public int getSize(){
        return wpl.size();
    }

    // only for tests
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
}
