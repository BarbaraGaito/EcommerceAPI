package com.uade.tpo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Product;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Autowired
    private CartService cartService;  

    @Autowired
    private ProductService productService; 

    @Override
    public List<Product> filterByCategory(String description) {
        return productService.getAllProducts().stream()
                .filter(product -> product.getCategory() != null && 
                                   product.getCategory().getDescription().equalsIgnoreCase(description))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProductsFromCatalog() {
        return productService.getAllProducts();
    }

    @Override
    public ResponseEntity<String> addToCart(Long carritoId, Long productId, int quantity) {
        try {
            
            Product product = productService.getProductById(productId);

            if (product.getStock() < quantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock insuficiente para este producto");
            }
            
            cartService.addProductToCart(carritoId, productId, quantity);
            return ResponseEntity.ok("Producto agregado al carrito exitosamente");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
