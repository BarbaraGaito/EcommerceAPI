package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setStock(product.getStock());
            updatedProduct.setPhotos(product.getPhotos());
            updatedProduct.setCategory(product.getCategory());
            return productRepository.save(updatedProduct);
        } else {
            throw new RuntimeException("Product not found with id " + id);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Implementación de los nuevos métodos

    @Override
    public Double calculateFinalPrice(Long id) {
        Product product = getProductById(id);
        if (product.getDiscount() != null && product.getDiscount() > 0) {
            return product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
        }
        return product.getPrice();
    }

    @Override
    public void applyDiscount(Long id, Double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        Product product = getProductById(id);
        product.setDiscount(discount);
        productRepository.save(product);
    }
}
