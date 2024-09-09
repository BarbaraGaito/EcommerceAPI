package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.dto.ProductDTO;

public interface CatalogoService {
    List<ProductDTO> getAllProductsFromCatalog();
    List<ProductDTO> filterByCategory(Long categoryId);
    List<ProductDTO> filterByPrice(Double minPrice, Double maxPrice);
    void addProductToCart(Long cartId, Long productId, int quantity);
}
