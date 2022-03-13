package com.symbizsolutions.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Log4j2
@SpringBootApplication
public class InsuranceApp {
    public static void main(String... args) {
        SpringApplication.run(InsuranceApp.class, args);
        log.info("app working fine");

    }
}
