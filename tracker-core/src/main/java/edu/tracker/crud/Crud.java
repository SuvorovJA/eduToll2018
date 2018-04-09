package edu.tracker.crud;


import edu.dto.PointDTO;
import edu.tracker.repository.PointDTOEntity;
import edu.tracker.repository.repo.PointDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Crud {

    private static final Logger log = LoggerFactory.getLogger(Crud.class);

    private List<PointDTOEntity> all;

    @Autowired
    PointDTORepository pointDTORepository;


    // other methods no need this

    public PointDTOEntity create(PointDTO pointObj) {
        PointDTOEntity point = new PointDTOEntity();
        point.setAutoId(pointObj.getAutoId());
        point.setLat(pointObj.getLat());
        point.setLon(pointObj.getLon());
        point.setTime(pointObj.getTime());
        return pointDTORepository.save(point);
    }

}
