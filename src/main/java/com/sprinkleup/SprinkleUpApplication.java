package com.sprinkleup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SprinkleUpApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        // Lets Hibernate's proxy library run on JDKs newer than it officially knows about.
        System.setProperty("net.bytebuddy.experimental", "true");
        SpringApplication.run(SprinkleUpApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SprinkleUpApplication.class);
    }
}
