package com.uade.tpo.demo.controllers.products;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.demo.Entity.Category;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.ProductImage;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.service.CategoryService;
import com.uade.tpo.demo.service.ProductService;
@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    
    @PostMapping
    public ResponseEntity<Product> createProduct(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam double price,
        @RequestParam double discount,
        @RequestParam int stock,
        @RequestParam Long categoryId,
        @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException, SQLException{

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setStock(stock);
        product.setImages(new ArrayList<>());
        
        Category category = categoryService.getCategoryById(categoryId).orElse(null);

        if (category != null) {
            product.setCategory(category);
        }

        for (MultipartFile image : images) {
            ProductImage productImage = new ProductImage();

            Blob blob = new SerialBlob(image.getBytes());
            productImage.setImage(blob); 
            
            productImage.setProduct(product);
            product.getImages().add(productImage);
        }

        productService.createProduct(product);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double discount,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException, SQLException {
    

        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
        if (name != null) product.setName(name);
        if (description != null) product.setDescription(description);
        if (price != null) product.setPrice(price);
        if (discount != null) product.setDiscount(discount);
        if (stock != null) product.setStock(stock);

        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId).orElse(null);
            if (category != null) {
                product.setCategory(category);
            }
        }
    
        if (images != null) {
            product.getImages().clear();
            for (MultipartFile image : images) {
                ProductImage productImage = new ProductImage();
                Blob blob = new SerialBlob(image.getBytes());
                productImage.setImage(blob);
                productImage.setProduct(product);
                product.getImages().add(productImage);
            }
        }
    
        productService.updateProduct(id, product);

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

        ProductDTO productoActualizado = new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getDiscount(),
                product.getCategory() != null ? product.getCategory().getDescription() : null,
                imageStrings 
            );

        
        return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductByIdDTO(@PathVariable Long id) {
        ProductDTO product = productService.getProductByIdDTO(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{id}/final-price")
    public ResponseEntity<Double> calculateFinalPrice(@PathVariable Long id) {
        Double finalPrice = productService.calculateFinalPrice(id);
        return new ResponseEntity<>(finalPrice, HttpStatus.OK);
    }

}
