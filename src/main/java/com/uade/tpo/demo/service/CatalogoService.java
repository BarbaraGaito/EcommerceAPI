package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.dto.ProductDTO;

public interface CatalogoService {
    List<ProductDTO> filterByCategory(Long categoryId);
    List<ProductDTO> getAllProductsFromCatalog();
    void addProductToCart(Long cartId, Long productId, int quantity);
}
