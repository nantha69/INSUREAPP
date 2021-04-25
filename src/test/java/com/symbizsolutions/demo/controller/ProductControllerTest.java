package com.symbizsolutions.demo.controller;

import static com.symbizsolutions.demo.entity.CountryCode.SG;
import static com.symbizsolutions.demo.entity.CountryCode.US;
import static com.symbizsolutions.demo.entity.InsuranceProvider.AIA;
import static com.symbizsolutions.demo.entity.InsuranceProvider.AVIVA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.exception.ResourceNotFoundException;
import com.symbizsolutions.demo.repository.ProductRepository;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductControllerTest {

    @Mock private ProductRepository repository;
    private ProductController controller;
    public Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductController(repository);
        product = new Product("1", AIA, "Health Sheild Plan", "5.00", SG);
    }

    @Test
    void listShouldFindAllFromRepo() {
        final ArrayList<Product> products = new ArrayList<>();
        when(repository.findAll()).thenReturn(products);
        assertThat(controller.list(null)).isEqualTo(products);
    }

    @Test
    void saveShouldSaveProductToRepo() {
        controller.save(product);
        verify(repository).save(product);
    }

    @Test
    void findProductByIdShouldReturnProductIfExists() {
        when(repository.findById("1")).thenReturn(Optional.of(product));
        assertThat(controller.find("1")).isEqualTo(product);
    }

    @Test
    void findProductByIdShouldThrowExceptionIfProductDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, ()-> controller.find("1"));
    }

    @Test
    void replaceShouldUpdateExistingProduct() {
        Product newProduct = new Product("1", AVIVA, "Aviva Premium Plan", "8.00", US);
        when(repository.findById("1")).thenReturn(Optional.of(product));
        when(repository.save(newProduct)).thenReturn(newProduct);
        final Product actual = controller.replaceProduct(newProduct, "1");
        assertThat(actual).isEqualTo(newProduct);
    }

    @Test
    void replaceShouldCreateNewProductIfNotExist() {
        Product newProduct = new Product("1", AVIVA, "Aviva Premium Plan", "8.00", US);
        when(repository.save(newProduct)).thenReturn(newProduct);
        final Product actual = controller.replaceProduct(newProduct, "1");
        assertThat(actual).isEqualTo(newProduct);
    }

    @Test
    void deleteProductShouldDeleteFromRepo() {
        controller.deleteProduct("1");
        verify(repository).deleteById("1");
    }
}
