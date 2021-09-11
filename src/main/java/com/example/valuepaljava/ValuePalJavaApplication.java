package com.example.valuepaljava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ValuePalJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValuePalJavaApplication.class, args);
    }

}
