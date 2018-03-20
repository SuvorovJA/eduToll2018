package edu.server.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by jdev on 07.03.2017.
 * Copy by sua on 05.03.2018, mod 20-03-2018
 */
@SpringBootApplication
@ComponentScan("edu.server.ui.config")
public class Main {
    public static void main(String... args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
