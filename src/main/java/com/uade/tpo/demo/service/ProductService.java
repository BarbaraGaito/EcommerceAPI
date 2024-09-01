package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.Product;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    List<Product> getAllProducts();

    // Métodos adicionales
    Double calculateFinalPrice(Long id);
    void applyDiscount(Long id, Double discount);
}
