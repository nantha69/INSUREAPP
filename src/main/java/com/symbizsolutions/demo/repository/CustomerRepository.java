package com.symbizsolutions.demo.repository;

import com.symbizsolutions.demo.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
