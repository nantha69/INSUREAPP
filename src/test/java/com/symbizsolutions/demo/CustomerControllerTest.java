package com.symbizsolutions.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.symbizsolutions.demo.entity.Customer;
import com.symbizsolutions.demo.exception.ResourceNotFoundException;
import com.symbizsolutions.demo.repository.CustomerRepository;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CustomerControllerTest {

    @Mock private CustomerRepository repository;
    private CustomerController controller;
    public Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CustomerController(repository);
        customer = new Customer(1L, "Bob", LocalDate.of(199, 10, 10), Customer.Gender.Male);
    }

    @Test
    void listShouldFindAllFromRepo() {
        final ArrayList<Customer> customers = new ArrayList<>();
        when(repository.findAll()).thenReturn(customers);
        assertThat(controller.list()).isEqualTo(customers);
    }

    @Test
    void saveShouldSaveCustomerToRepo() {
        controller.save(customer);
        verify(repository).save(customer);
    }

    @Test
    void findCustomerByIdShouldReturnCustomerIfExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        assertThat(controller.find(1L)).isEqualTo(customer);
    }

    @Test
    void findCustomerByIdShouldThrowExceptionIfCustomerDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, ()-> controller.find(1L));
    }

    @Test
    void replaceShouldUpdateExistingCustomer() {
        Customer newCustomer = new Customer("Alice", LocalDate.of(1998,1,1), Customer.Gender.Female);
        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        when(repository.save(newCustomer)).thenReturn(newCustomer);
        final Customer actual = controller.replaceCustomer(newCustomer, 1L);
        assertThat(actual).isEqualTo(newCustomer);
    }

    @Test
    void replaceShouldCreateNewCustomerIfNotExist() {
        Customer newCustomer = new Customer("Alice", LocalDate.of(1998,1,1), Customer.Gender.Female);
        when(repository.save(newCustomer)).thenReturn(newCustomer);
        final Customer actual = controller.replaceCustomer(newCustomer, 1L);
        assertThat(actual).isEqualTo(newCustomer);
    }

    @Test
    void deleteCustomerShouldDeleteFromRepo() {
        controller.deleteCustomer(1L);
        verify(repository).deleteById(1L);
    }
}
