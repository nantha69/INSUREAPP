package com.symbizsolutions.demo.controller;

import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.exception.ResourceNotFoundException;
import com.symbizsolutions.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository repository;
    @GetMapping("/products")
    List<Product> list() {
        return repository.findAll();
    }

    @PostMapping("/product")
    Product save(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }

    @GetMapping("/product/{id}")
    Product find(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @PutMapping("/product/{id}")
    Product replaceProduct(@RequestBody Product newProduct, @PathVariable Long id) {

        return repository.findById(id)
                .map(product -> {
                    product.setProvider(newProduct.getProvider());
                    product.setPlan(newProduct.getPlan());
                    product.setPremiumRate(newProduct.getPremiumRate());
                    product.setCountryCode(newProduct.getCountryCode());
                    return repository.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
    }

    @DeleteMapping("/product/{id}")
    void deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
