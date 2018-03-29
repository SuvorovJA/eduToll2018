package edu.tracker.services;

import edu.dto.PointDTO;
import edu.tracker.storage.QueueGPS;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DataPeekTest {

    // string from file
    private String expected = "{\"lat\":54.109707,\"lon\":54.105139,\"autoId\":\"E555EM70\",\"time\":1282361025400}";
    private String readable;
    private GpsService gpsService = null;
    private PointDTO localpoint = null;
    private QueueGPS queueGPS = null;
    // tested
    private DataPeek dataPeek = null;

    @Test
    public void fetchGps_integration() throws IOException, InterruptedException {
        // очередь
        queueGPS = new QueueGPS();
        // гпс сервис
        gpsService = new GpsService();
        gpsService.setFileName("11060.gpx");
        gpsService.init();
        // тесты
        assertEquals(1441L, gpsService.getSize()); // должно быть считано столько точек
        assertEquals(0L, queueGPS.getSize()); // очередь должна быть пуста
        dataPeek = new DataPeek(queueGPS, gpsService); // tested (get point from file, put point to queue
        dataPeek.fetchGps();
        assertEquals(1L, queueGPS.getSize()); // в очереди одна точка
        localpoint = queueGPS.take(); // забрать точку из очереди
        assertEquals(0L, queueGPS.getSize()); // очередь должна быть пуста
        assertEquals(expected, localpoint.toJson()); // точка должна совпасть с ожидаемой
    }
}