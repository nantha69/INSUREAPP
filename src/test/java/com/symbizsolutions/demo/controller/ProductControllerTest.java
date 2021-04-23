package com.symbizsolutions.demo.controller;

import com.symbizsolutions.demo.entity.Customer;
import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.exception.ResourceNotFoundException;
import com.symbizsolutions.demo.repository.CustomerRepository;
import com.symbizsolutions.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static com.symbizsolutions.demo.entity.Product.CountryCode.US;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Mock private ProductRepository repository;
    private ProductController controller;
    public Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductController(repository);
        product = new Product(1L, "AIA", "Health Sheild Plan", "5.00", Product.CountryCode.SG);
    }

    @Test
    void listShouldFindAllFromRepo() {
        final ArrayList<Product> products = new ArrayList<>();
        when(repository.findAll()).thenReturn(products);
        assertThat(controller.list()).isEqualTo(products);
    }

    @Test
    void saveShouldSaveProductToRepo() {
        controller.save(product);
        verify(repository).save(product);
    }

    @Test
    void findProductByIdShouldReturnProductIfExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        assertThat(controller.find(1L)).isEqualTo(product);
    }

    @Test
    void findProductByIdShouldThrowExceptionIfProductDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, ()-> controller.find(1L));
    }

    @Test
    void replaceShouldUpdateExistingProduct() {
        Product newProduct = new Product(1L,"Aviva", "Aviva Premium Plan", "8.00",US);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(newProduct)).thenReturn(newProduct);
        final Product actual = controller.replaceProduct(newProduct, 1L);
        assertThat(actual).isEqualTo(newProduct);
    }

    @Test
    void replaceShouldCreateNewProductIfNotExist() {
        Product newProduct = new Product(1L,"Aviva", "Aviva Premium Plan", "8.00",US);
        when(repository.save(newProduct)).thenReturn(newProduct);
        final Product actual = controller.replaceProduct(newProduct, 1L);
        assertThat(actual).isEqualTo(newProduct);
    }

    @Test
    void deleteProductShouldDeleteFromRepo() {
        controller.deleteProduct(1L);
        verify(repository).deleteById(1L);
    }
}
