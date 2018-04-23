package edu.tracker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dto.PointDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@RestController
public class SendController {

    private static final Logger log = LoggerFactory.getLogger(SendController.class);

//    private PointDTO point;
    private final Random random = new Random();

    @Value("${send.controller.dbserver}")
    private String dbServerURL;

    RestTemplate restTemplate;

    public SendController(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
//        this.point = null;
    }

    @Async
    public ResponseEntity<PointDTO> setPoint(PointDTO point) {
        int random_marker = random.nextInt();
        int random_priority = random.nextInt(3);
        log.info("@Async/setPoint()/" + random_marker + "/" + random_priority);
//        this.point = point; // ненене, с Асинк будет отправляться неизвестно что. синглтон-же.
        return postSend(point, random_marker, random_priority);
    }

    //@Async не ставится здесь. пусть будет в одном потоке с setPoint(), да и не изменится поток всё равно
    private ResponseEntity<PointDTO> postSend(PointDTO pointDTO, int marker, int priority) {
        switch (priority){
            case 0: Thread.currentThread().setPriority(Thread.NORM_PRIORITY); break;
            case 1: Thread.currentThread().setPriority(Thread.MIN_PRIORITY); break;
            case 2: Thread.currentThread().setPriority(Thread.MAX_PRIORITY); break;
        }
        log.info("@NotAsync/postSend()/" + marker+ "/" + Thread.currentThread().getPriority());
        // сохранение в БД
        //
        boolean statusOk = true;
        String sendingPointInJson = "{}";
        PointDTO pointDTOreturned = null;
        //
        try {
            sendingPointInJson = pointDTO.toJson();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        // Represents an HTTP request or response entity, consisting of headers and body. (магия?)
        HttpEntity<PointDTO> entity = new HttpEntity<PointDTO>(pointDTO);
        // Extension of HttpEntity that adds a HttpStatus status code.
        ResponseEntity<PointDTO> response = null;
        try {
            response = restTemplate.postForEntity(dbServerURL, entity, PointDTO.class);
            try {
                pointDTOreturned = response.getBody();
                log.info("FULL RESPONSE/" + marker + " " + response.toString()); // log.debug выключен в logback.xml, генерирует большой логгинг
            } catch (NullPointerException e) {
                log.error(e.getMessage());
            }
        } catch (RestClientException e) {
            log.error("LOST POINT/" + marker + " " + sendingPointInJson + ", CAUSE " + e.getMessage());
            statusOk = false;
        }
        // можно сравнить отправленный объект и принятый в эхе, и при разночтениях что то предпринять
        if (statusOk) {
            try {
                log.info("SEND/" + marker + " " + sendingPointInJson);
                log.info("ECHO/" + marker + " " + pointDTOreturned.toJson());
            } catch (NullPointerException | JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        return response;
    }

    public void setDbServerURL(String dbServerURL) {
        this.dbServerURL = dbServerURL;
    }

}