package edu.tracker.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dto.PointDTO;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class QueueGPSTest {

    private String expected = "{\"lat\":56.0,\"lon\":74.0,\"autoId\":\"o567gfd\",\"time\":1489900897458}";
    private PointDTO localpoint1 = null;
    private PointDTO localpoint2 = null;
    private QueueGPS queueGPS = null;
    private ObjectMapper mapper = null;

    @Test
    public void take_put_getSize() throws IOException, InterruptedException {
        mapper = new ObjectMapper();
        queueGPS = new QueueGPS();
        localpoint1 = mapper.readValue(expected, PointDTO.class);
        queueGPS.put(localpoint1);
        assertEquals(1L,queueGPS.getSize());
        localpoint2 = queueGPS.take();
        assertEquals(0L,queueGPS.getSize());
        assertEquals(expected,localpoint2.toJson());
    }

}