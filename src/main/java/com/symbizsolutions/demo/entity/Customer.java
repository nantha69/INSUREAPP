package com.symbizsolutions.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    public enum Gender {
        Male,
        Female,
        Other
    }
    private @Id @GeneratedValue Long id;
    private String name;
    private LocalDate dob;
    private Gender gender;

    public Customer(final String name, final LocalDate dob, final Gender gender) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
    }
}
