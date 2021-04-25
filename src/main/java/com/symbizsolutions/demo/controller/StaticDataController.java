package com.symbizsolutions.demo.controller;

import java.util.Arrays;
import java.util.List;

import com.symbizsolutions.demo.entity.CountryCode;
import com.symbizsolutions.demo.entity.Gender;
import com.symbizsolutions.demo.entity.InsuranceProvider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaticDataController {
    @GetMapping("/genders")
    List<Gender> genders() {
        return Arrays.asList(Gender.values());
    }
    @GetMapping("/countries")
    List<CountryCode> countries() {
        return Arrays.asList(CountryCode.values());
    }
    @GetMapping("/insuranceproviders")
    List<InsuranceProvider> insuranceproviders() {
        return Arrays.asList(InsuranceProvider.values());
    }
}
