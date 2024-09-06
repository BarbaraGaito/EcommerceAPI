package com.uade.tpo.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.uade.tpo.demo.Entity.Product;

public interface CatalogoService {
    List<Product> filterByCategory(String description);
    ResponseEntity<String> addToCart(Long carritoId, Long productId, int quantity); 
    List<Product> getAllProductsFromCatalog();
}
