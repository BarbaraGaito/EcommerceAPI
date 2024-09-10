package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.dto.ProductDTO;

public interface ProductService {
    void createProduct(Product product);
    void updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    ProductDTO getProductByIdDTO(Long id);
    Double calculateFinalPrice(Long id);
}
