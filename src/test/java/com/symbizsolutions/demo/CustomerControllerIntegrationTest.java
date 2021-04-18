package com.symbizsolutions.demo;

import static com.symbizsolutions.demo.entity.Customer.Gender.Female;
import static com.symbizsolutions.demo.entity.Customer.Gender.Male;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.symbizsolutions.demo.entity.Customer;
import com.symbizsolutions.demo.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CustomerRepository repository;

    public Customer customer1;
    public Customer customer2;

    @BeforeEach
    void setUp() {
        customer1 = new Customer(
                1L,
                "Bilbo Baggins",
                LocalDate.of(1983, 10, 20),
                Male
        );
        customer2 = new Customer(
                2L,
                "Mary Baggins",
                LocalDate.of(1983, 12, 1),
                Female
        );
    }

    @Test
    @Order(1)
    void testSaveCustomer() {
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/customers",
                                                   new Customer("Bilbo Baggins", LocalDate.of(1983, 10, 20),
                                                                Male),Customer.class))
                   .isEqualTo(customer1);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/customers",
                                                   new Customer( "Mary Baggins", LocalDate.of(1983,12,1),
                                                                Female),Customer.class))
                .isEqualTo(customer2);
    }

    @Test
    @Order(2)
    void testAllCustomers() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customers",
                                                  Customer[].class)).isEqualTo(
                                                          new Customer[] {
                                                                  customer1,
                                                                  customer2
                                                          }
        );
    }

    @Test
    @Order(3)
    void testCustomerWithId() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customers/1",
                                                  Customer.class)).isEqualTo(customer1);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customers/2",
                                                  Customer.class)).isEqualTo(customer2);
    }

    @Test
    @Order(4)
    void testDeleteCustomerWithId() {
       this.restTemplate.delete("http://localhost:" + port + "/customers/1");
       this.restTemplate.delete("http://localhost:" + port + "/customers/2");
       assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customers",
                                                  Customer[].class)).isEqualTo(new Customer[]{});
    }

}
