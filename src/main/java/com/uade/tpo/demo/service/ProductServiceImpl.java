package com.uade.tpo.demo.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, Product product) {
        Optional<Product> databaseProduct = productRepository.findById(id);
        if (databaseProduct.isPresent()) {
            Product existingProduct = databaseProduct.get();
            
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDiscount(product.getDiscount());

            if (product.getImages() != null && !product.getImages().isEmpty()) {
                existingProduct.setImages(product.getImages());
            }

            productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Producto no encontrado para el id:" + id);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado para el id: " + id));
    }

    @Override
    public ProductDTO getProductByIdDTO(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        
        List<String> imageStrings = product.getImages().stream()
            .map(image -> {
                try {
                    Blob blob = image.getImage();
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    return Base64.getEncoder().encodeToString(imageBytes);
                } catch (SQLException e) {
                    throw new RuntimeException("Error reading image", e);
                }
            })
            .collect(Collectors.toList());

        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getDiscount(),
            product.getCategory() != null ? product.getCategory().getDescription() : null,
            imageStrings 
        );
    }

    



    @Override
    public Double calculateFinalPrice(Long id) {
        Product product = getProductById(id);
        if (product.getDiscount() != null && product.getDiscount() > 0) {
            return product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
        }
        return product.getPrice();
    }

}
