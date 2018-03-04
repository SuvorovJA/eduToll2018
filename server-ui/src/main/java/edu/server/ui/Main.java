package edu.server.ui;

import edu.dto.PointDTO;

/**
 * Created by jdev on 07.03.2017.
 * Copy by sua on 05.03.2018
 */
public class Main {
    public static void main(String... args) throws Exception {
        for (int i=0; i<1; i++) {
            System.out.println("Server-ui.main say Hello!!!!");
            PointDTO point = new PointDTO();
            point.setLat(45);
            System.out.println(point.toJson());
            Thread.sleep(1000);
        }
    }
}