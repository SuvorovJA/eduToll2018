package edu.server;

import edu.server.context.InjectionContext;
import org.springframework.boot.SpringApplication;


/**
 * Created by jdev on 07.03.2017.
 * Copy by sua on 05.03.2018, Mod 15-03-2018, Mod 09-04-2018
 */

public class Main {

    public static void main(String[] args) {
        SpringApplication.run(InjectionContext.class, args);
    }


}
