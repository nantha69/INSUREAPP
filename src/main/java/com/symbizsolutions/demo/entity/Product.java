package com.symbizsolutions.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    public enum CountryCode {
        SG,
        US,
        UK
    }
    private @Id
    @GeneratedValue
    Long id;
    private String provider;
    private String plan;
    private BigDecimal premiumRate;
    private CountryCode countryCode;

    public Product(Long id,String provider, String plan, String premiumRate, CountryCode countryCode) {
        this.id=id;
        this.provider = provider;
        this.plan = plan;
        this.premiumRate = new BigDecimal(premiumRate);
        this.countryCode = countryCode;
    }
}
