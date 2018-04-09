package edu.server.crud;


import edu.dto.PointDTO;
import edu.server.repository.PointDTOEntity;
import edu.server.repository.repo.PointDTORepository;
//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@Slf4j
public class Crud {

    private static final Logger log = LoggerFactory.getLogger(Crud.class);

    private List<PointDTOEntity> all;

    @Autowired
    PointDTORepository pointDTORepository;

    public void delete(PointDTOEntity point) {
        pointDTORepository.delete(point);
    }

    public void update(PointDTOEntity point, PointDTO pointObj) {
        point.setAutoId(pointObj.getAutoId());
        point.setLat(pointObj.getLat());
        point.setLon(pointObj.getLon());
        point.setTime(pointObj.getTime());
        pointDTORepository.save(point);
    }

    // можно зашедулить для мониторинга содержимого таблицы если не жалко лога
    public void read() {
        // будет мегадлинный-же вывод
        all = (List<PointDTOEntity>) pointDTORepository.findAll();
        if (all.size() == 0) {
            log.info("NO RECORDS");
        } else {
            all.stream().forEach(point -> log.info(point.toString()));
        }
    }


    //в логи прилетает (при включенном дебаге гибернейта)
    //      2018-04-09 20:54:00.261 DEBUG 12711 --- [nio-8090-exec-8] org.hibernate.SQL
    //                  : insert into points (id, autoid, lat, lon, time) values (null, ?, ?, ?, ?)
    //однако эхо из этого метода и записи в БД нормальные                        ~~~~~~~~~~~~~~~~~~~
    public PointDTOEntity create(PointDTO pointObj) {
        PointDTOEntity point = new PointDTOEntity();
        point.setAutoId(pointObj.getAutoId());
        point.setLat(pointObj.getLat());
        point.setLon(pointObj.getLon());
        point.setTime(pointObj.getTime());
        return pointDTORepository.save(point);
    }

}
