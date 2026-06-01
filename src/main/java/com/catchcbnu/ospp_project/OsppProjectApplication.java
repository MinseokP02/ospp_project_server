package com.catchcbnu.ospp_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OsppProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsppProjectApplication.class, args);
    }

}
