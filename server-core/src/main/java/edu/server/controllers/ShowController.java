package edu.server.controllers;


import edu.server.crud.Crud;
import edu.server.repository.PointDTOEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ShowController {

    @Autowired
    private Crud crud;

    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Welcome to server-core RESTful service. <br> " +
                "use <br>" +
                "http://localhost:8090/show <br>" +
                "http://localhost:8090/show?num=2100 <br>" +
                "http://localhost:8090/show?num=21&auto=A123BC78 <br>";
    }


    @RequestMapping(value = "/show", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<PointDTOEntity> getPoints(
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "auto", defaultValue = "all") String autoid) {
        log.info("params=" + num + ", " + autoid);
        List<PointDTOEntity> list;
        if (autoid.contentEquals("all")) {
            list = crud.readLastN(num);
        } else {
            list = crud.readLastNForAutoid(num, autoid);
        }
        return list;
    }


}
