package com.symbizsolutions.demo.controller;

import static com.symbizsolutions.demo.entity.CountryCode.HK;
import static com.symbizsolutions.demo.entity.CountryCode.UK;
import static com.symbizsolutions.demo.entity.CountryCode.US;
import static com.symbizsolutions.demo.entity.InsuranceProvider.AIA;
import static com.symbizsolutions.demo.entity.InsuranceProvider.AVIVA;
import static com.symbizsolutions.demo.entity.InsuranceProvider.GreatEastern;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.symbizsolutions.demo.config.LoadDatabase;
import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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
        product1 = new Product("1", AIA, "Health Sheild Plan", "5.00", US);
        product2 = new Product("2", AVIVA, "Aviva Premium Plan", "8.00", UK);
    }

    @Test
    @Order(0)
    void testAllProducts() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/products",
                                                  Product[].class)).isEqualTo(LoadDatabase.DEFAULT_PRODUCTS);
    }

    @Test
    @Order(0)
    void testProductsByCountry() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/products?countryCode=HK",
              Product[].class)).isEqualTo(new Product[] {
                new Product("GEHKSTD", GreatEastern, "Great Eastern Savings Plan", "3.21", HK)});
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
                                                  Product[].class)).isEqualTo(LoadDatabase.DEFAULT_PRODUCTS);
    }

}
