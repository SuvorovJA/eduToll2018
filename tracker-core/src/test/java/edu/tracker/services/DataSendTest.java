package edu.tracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dto.PointDTO;
import edu.tracker.controllers.SendController;
import edu.tracker.storage.QueueGPS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataSendTest {

    // string from file
    private String expected1 = "{\"lat\":54.109707,\"lon\":54.105139,\"autoId\":\"E555EM70\",\"time\":1282361025400}";
    private String expected2 = "{\"lat\":54.109641,\"lon\":54.105211,\"autoId\":\"E555EM70\",\"time\":1282361062000}";
    private PointDTO localpoint1 = null;
    private PointDTO localpoint2 = null;
    private PointDTO returnedpoint1 = null;
    private PointDTO returnedpoint2 = null;
    private QueueGPS queueGPS = null;
    private ObjectMapper mapper = null;
//    private DataSend dataSend = null;

    @Mock
    SendController sendController; // это подменяется

    @InjectMocks
    DataSend dataSend; // то что подменяется вствляется сюда через конструктор

    @Test
    public void sendToDB() throws IOException, InterruptedException {
        // очередь
        queueGPS = new QueueGPS();
        // points
        mapper = new ObjectMapper();
        localpoint1 = mapper.readValue(expected1, PointDTO.class);
        localpoint2 = mapper.readValue(expected2, PointDTO.class);
        // place points to queue
        assertEquals(0L,queueGPS.getSize());
        queueGPS.put(localpoint1);
        queueGPS.put(localpoint2);
        queueGPS.put(localpoint2);
        assertEquals(3L,queueGPS.getSize()); // two point on start
        // test target
        dataSend = new DataSend(queueGPS,sendController);
        dataSend.sendToDB();
        //
        // queue must be empty
        assertEquals(0L,queueGPS.getSize());
        // внутри sendToDB() по одному/два разу вызывались setPoint() ?
        verify(sendController, times(1)).setPoint(localpoint1);
        verify(sendController, times(2)).setPoint(localpoint2);
        verify(sendController, times(0)).setPoint(null);
    }
}