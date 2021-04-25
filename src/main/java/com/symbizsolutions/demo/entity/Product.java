package com.symbizsolutions.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private @Id String id;
    private InsuranceProvider provider;
    private String plan;
    private BigDecimal premiumRate;
    private CountryCode countryCode;

    public Product(String id,InsuranceProvider provider, String plan, String premiumRate, CountryCode countryCode) {
        this.id=id;
        this.provider = provider;
        this.plan = plan;
        this.premiumRate = new BigDecimal(premiumRate);
        this.countryCode = countryCode;
    }
}
