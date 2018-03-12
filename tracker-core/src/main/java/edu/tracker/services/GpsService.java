/**
 * по запросу от DataPeek должен выдать координатную точку
 *
 *
 */
package edu.tracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class GpsService {

    @Value("${gpx.source}")
    private String fileName;

    @PostConstruct
    public void init() {
        System.out.println("GpsService gpx.source = " + fileName);
    }

}
