package com.symbizsolutions.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.symbizsolutions.demo.entity.CountryCode;
import com.symbizsolutions.demo.entity.Product;
import com.symbizsolutions.demo.exception.ResourceNotFoundException;
import com.symbizsolutions.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository repository;
    @GetMapping("/products")
    List<Product> list(@RequestParam(required = false) CountryCode countryCode) {
        if (countryCode!=null) {
            return repository.findAll().stream().filter(p -> p.getCountryCode() == countryCode).collect(Collectors.toList());
        }
        else {
            return repository.findAll();
        }
    }

    @PostMapping("/product")
    Product save(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }

    @GetMapping("/product/{id}")
    Product find(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @PutMapping("/product/{id}")
    Product replaceProduct(@RequestBody Product newProduct, @PathVariable String id) {

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
    void deleteProduct(@PathVariable String id) {
        repository.deleteById(id);
    }
}
