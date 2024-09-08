package com.uade.tpo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.repository.ProductRepository;


@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Autowired
    private CartService cartService;  

    @Autowired
    private ProductService productService; 

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDTO> filterByCategory(Long categoryId) {
        return productRepository.findAll().stream()
            .filter(product -> product.getCategory() != null && product.getCategory().getId().equals(categoryId))
            .map(product -> new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory() != null ? product.getCategory().getDescription() : null))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllProductsFromCatalog() {
        // Obtener todos los productos desde el repositorio de productos
        List<Product> products = productRepository.findAll();

        // Convertir a DTO para evitar referencias circulares y devolver los productos simplificados
        return products.stream()
                .map(product -> new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategory() != null ? product.getCategory().getDescription() : null))
                .collect(Collectors.toList());
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
