package edu.tracker;

import edu.tracker.context.InjectionContext;
import org.springframework.boot.SpringApplication;

/**
 * Created by jdev on 07.03.2017.
 * Copy by sua on 05.03.2018
 * Mod by sua on 12.03.2016, 16-03-2018
 */

public class Main {
    public static void main(String... args) throws Exception {
        SpringApplication.run(InjectionContext.class, args);
    }

}
