package com.symbizsolutions.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private @Id String id;
    private String name;
    private LocalDate dob;
    private Gender gender;
    private CountryCode countryCode;

    public Customer(String name, LocalDate dob, Gender gender, CountryCode countryCode) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.countryCode = countryCode;
    }
}
