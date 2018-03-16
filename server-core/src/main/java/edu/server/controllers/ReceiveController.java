package edu.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dto.PointDTO;
import edu.server.services.OpenFileForWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ReceiveController {

    private static final Logger log = LoggerFactory.getLogger(ReceiveController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OpenFileForWrite fileForWrite;

    // здесь в запросе прилетает сразу точка, принять и сохранить
    @RequestMapping(value = "/receiver", method = RequestMethod.POST)
    public @ResponseBody
    PointDTO addPoint(@RequestBody PointDTO localpoint) {
        try {
            // сохранить в БД (вывести в лог и в файл)
            log.info("RECEIVED POINT " + localpoint.toJson());
            fileForWrite.writeln(localpoint.toJson());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return localpoint;
    }

}
