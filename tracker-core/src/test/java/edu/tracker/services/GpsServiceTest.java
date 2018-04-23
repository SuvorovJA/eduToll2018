package edu.tracker.services;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class GpsServiceTest {

    // string from file
    private String expected = "{\"lat\":54.109707,\"lon\":54.105139,\"autoId\":\"E555EM70\",\"time\":1282361025400}";
    private GpsService gpsService = null;

    @Test
    public void init_then_getNext() throws IOException {
        gpsService = new GpsService();
        gpsService.setFileName("11060.gpx");
        gpsService.init();
        assertEquals(1441L, gpsService.getSize());
        assertEquals(expected, gpsService.getNext(0)); // TODO fix that 0
    }
}