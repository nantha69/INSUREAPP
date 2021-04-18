package com.symbizsolutions.demo.config;

import static com.symbizsolutions.demo.entity.Customer.Gender.Female;
import static com.symbizsolutions.demo.entity.Customer.Gender.Male;

import java.time.LocalDate;

import com.symbizsolutions.demo.entity.Customer;
import com.symbizsolutions.demo.repository.CustomerRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
//@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(CustomerRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Customer("Bilbo Baggins", LocalDate.of(1983,10,20),
                                                                  Male)));
            log.info("Preloading " + repository.save(new Customer("Mary Baggins", LocalDate.of(1983,12,1),
                                                                  Female)));
        };
    }
}
