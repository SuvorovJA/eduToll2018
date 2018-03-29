package edu.tracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dto.PointDTO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.Assert.*;

public class SendControllerTest {
/*
должен быть запущен Server-sore.Main, порт 8090
 */

    // string from file
    private String expected1 = "{\"lat\":54.109707,\"lon\":54.105139,\"autoId\":\"E555EM70\",\"time\":1282361025400}";
    private PointDTO localpoint1 = null;
    private RestTemplate restTemplate = null;
    private SendController sendController = null;
    private ResponseEntity<PointDTO> response = null;
    private ObjectMapper mapper = null;
    private HttpStatus httpStatus;

    @Test
    public void setPoint() throws IOException {
        // point
        mapper = new ObjectMapper();
        localpoint1 = mapper.readValue(expected1, PointDTO.class);
        //
        httpStatus = HttpStatus.valueOf(200);
        restTemplate = new RestTemplate();
        sendController = new SendController(restTemplate);
        sendController.setDbServerURL("http://localhost:8090/receiver");
        response = sendController.setPoint(localpoint1);
        assertNotNull(response);
//        assertEquals(localpoint1, response.getBody()); // send_obj == echo_obj ? адреса разные, не работает
        assertEquals(localpoint1.toJson(), response.getBody().toJson()); // send_obj.toJson == echo_obj.toJson ?
        assertEquals(httpStatus, response.getStatusCode());
    }
}