package com.symbizsolutions.demo.config;

import static com.symbizsolutions.demo.entity.InsuranceProvider.AIA;
import static com.symbizsolutions.demo.entity.InsuranceProvider.AXA;
import static com.symbizsolutions.demo.entity.InsuranceProvider.GreatEastern;
import static com.symbizsolutions.demo.entity.InsuranceProvider.NTUC;
import static com.symbizsolutions.demo.entity.InsuranceProvider.Singlife;

import java.util.Arrays;

import com.symbizsolutions.demo.entity.CountryCode;
import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class LoadDatabase {
    final static Product[] DEFAULT_PRODUCTS = {
            new Product("AIASGSTD", AIA, "AIA Life Standard Plan", "3.20", CountryCode.SG),
            new Product("AIAUSSTD",AIA,"AIA Life Standard Plan","2.20",CountryCode.US),
            new Product("AIAUKSTD",AIA,"AIA Life Standard Plan","1.20",CountryCode.UK),
            new Product("AXASGSTD",AXA,"AXA Life Standard Plan","3.30",CountryCode.SG),
            new Product("AXAUSSTD",AXA,"AXA Life Standard Plan","2.30",CountryCode.US),
            new Product("AXAUKSTD",AXA,"AIA Life Standard Plan","1.30",CountryCode.SG),
            new Product("AIASGPRM",AIA,"AIA Savings Premium Plan","4.25",CountryCode.SG),
            new Product("AIAUSPRM",AIA,"AIA Savings Premium Plan","3.25",CountryCode.US),
            new Product("AIAUKPRM",AIA,"AIA Savings Premium Plan","2.25",CountryCode.UK),
            new Product("AXASGPRM",AXA,"AXA Savings Premium Plan","4.35",CountryCode.SG),
            new Product("AXAUSPRM",AXA,"AXA Savings Premium Plan","2.35",CountryCode.US),
            new Product("AXAUKPRM",AXA,"AXA Savings Premium Plan","2.36",CountryCode.SG),
            new Product("GESGSTD",GreatEastern,"Great Eastern Savings Plan","3.21",CountryCode.SG),
            new Product("NTUCINCOME",NTUC,"NTUC Income Plan","4.26",CountryCode.SG),
            new Product("SINGLIFE",Singlife,"Hi Life Savings Plan","3.21",CountryCode.SG),
            new Product("GEHKSTD",GreatEastern,"Great Eastern Savings Plan","3.21",CountryCode.HK)

    };

    public static Product[] defaultProducts() {
        return Arrays.copyOf(DEFAULT_PRODUCTS,DEFAULT_PRODUCTS.length);
    }
    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> Arrays.stream(DEFAULT_PRODUCTS).forEach(p->log.info("Preloading " + repository.save(p)));
    }

}
