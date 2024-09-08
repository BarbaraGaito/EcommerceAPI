package com.uade.tpo.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.uade.tpo.demo.Entity.dto.ProductDTO;

public interface CatalogoService {
    List<ProductDTO> filterByCategory(Long categoryId);
    ResponseEntity<String> addToCart(Long carritoId, Long productId, int quantity); 
    List<ProductDTO> getAllProductsFromCatalog();
}
