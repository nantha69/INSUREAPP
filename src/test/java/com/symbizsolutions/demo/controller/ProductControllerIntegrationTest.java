package com.symbizsolutions.demo.controller;

import com.symbizsolutions.demo.entity.Customer;
import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.repository.CustomerRepository;
import com.symbizsolutions.demo.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDate;

import static com.symbizsolutions.demo.entity.Customer.Gender.Female;
import static com.symbizsolutions.demo.entity.Customer.Gender.Male;
import static com.symbizsolutions.demo.entity.Product.CountryCode.*;
import static com.symbizsolutions.demo.entity.Product.CountryCode.US;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository repository;

    public Product product1;
    public Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "AIA", "Health Sheild Plan", "5.00", SG);
        product2 = new Product(2L,"Aviva", "Aviva Premium Plan", "8.00",US);
    }

    @Test
    @Order(1)
    void testSaveProduct() {
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/product",
                                                   product1,Product.class))
                   .isEqualTo(product1);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/product",
                                                   product2,Product.class))
                .isEqualTo(product2);
    }

    @Test
    @Order(2)
    void testAllProducts() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/products",
                                                  Product[].class)).isEqualTo(
                                                          new Product[] {
                                                                  product1,
                                                                  product2
                                                          }
        );
    }

    @Test
    @Order(3)
    void testProductWithId() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/product/1",
                                                  Product.class)).isEqualTo(product1);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/product/2",
                                                  Product.class)).isEqualTo(product2);
    }

    @Test
    @Order(4)
    void testDeleteProductWithId() {
       this.restTemplate.delete("http://localhost:" + port + "/product/1");
       this.restTemplate.delete("http://localhost:" + port + "/product/2");
       assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/products",
                                                  Product[].class)).isEqualTo(new Product[]{});
    }

}
