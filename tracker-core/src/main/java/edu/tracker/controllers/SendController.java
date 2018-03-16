package edu.tracker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dto.PointDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RestController
public class SendController {

    private static final Logger log = LoggerFactory.getLogger(SendController.class);

    private PointDTO point;

    @Value("${send.controller.dbserver}")
    private String dbServerURL;

    @Autowired
    RestTemplate restTemplate;

    public void setPoint(PointDTO point) {
        this.point = point;
        postSend();
    }

    private void postSend() {
        // сохранение в БД
        //
        boolean statusOk = true;
        String sendingPointInJson = "{}";
        PointDTO pointDTOreturned = null;
        //
        try {
            sendingPointInJson = this.point.toJson();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        // Represents an HTTP request or response entity, consisting of headers and body. (магия?)
        HttpEntity<PointDTO> entity = new HttpEntity<PointDTO>(this.point);
        // Extension of HttpEntity that adds a HttpStatus status code.
        ResponseEntity<PointDTO> response = null;
        try {
            response = restTemplate.postForEntity(dbServerURL, entity, PointDTO.class);
            try {
                pointDTOreturned = response.getBody();
                log.info("DEBUG (FULL RESPONSE) " + response.toString()); // log.debug выключен в logback.xml, генерирует большой логгинг
            } catch (NullPointerException e) {
                log.error(e.getMessage());
            }
        } catch (RestClientException e) {
            log.error("LOST POINT " + sendingPointInJson + ", CAUSE " + e.getMessage());
            statusOk = false;
        }
        // можно сравнить отправленный объект и принятый в эхе, и при разночтениях что то предпринять
        if (statusOk) {
            try {
                log.info("SEND " + sendingPointInJson);
                log.info("ECHO " + pointDTOreturned.toJson());
            } catch (NullPointerException | JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }
}