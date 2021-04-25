package com.symbizsolutions.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.symbizsolutions.demo.entity.CountryCode;
import com.symbizsolutions.demo.entity.Gender;
import com.symbizsolutions.demo.entity.InsuranceProvider;

class StaticDataControllerTest {

    private StaticDataController controller = new StaticDataController();

    @Test
    void testGenders() {
        assertThat(controller.genders()).isEqualTo(Arrays.asList(Gender.values()));
    }

    @Test
    void countries() {
        assertThat(controller.countries()).isEqualTo(Arrays.asList(CountryCode.values()));
    }

    @Test
    void insuranceproviders() {
        assertThat(controller.insuranceproviders()).isEqualTo(Arrays.asList(InsuranceProvider.values()));
    }
}
