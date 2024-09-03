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
        Optional<Product> databaseProduct = productRepository.findById(id);
        if (databaseProduct.isPresent()) {
            Product existingProduct = databaseProduct.get();
            
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock(product.getStock());
            existingProduct.setCategory(product.getCategory());
            
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                existingProduct.setImages(product.getImages());
            }

            return productRepository.save(existingProduct);
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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

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
