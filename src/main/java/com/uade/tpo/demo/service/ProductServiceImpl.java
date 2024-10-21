package com.uade.tpo.demo.service;


import java.util.Optional;


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
    
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getDiscount(),
            product.getCategory() != null ? product.getCategory().getDescription() : null);
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
