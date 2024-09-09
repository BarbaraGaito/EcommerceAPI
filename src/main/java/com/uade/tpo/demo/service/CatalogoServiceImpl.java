package com.uade.tpo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.CartItem;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.ProductRepository;


@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Autowired
    private CartService cartService;  

    @Autowired
    private ProductService productService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProductsFromCatalog() {
        List<Product> products = productRepository.findAll();

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
    public List<ProductDTO> filterByPrice(Double minPrice, Double maxPrice) {
        return productRepository.findAll().stream()
            .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
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
    public void addProductToCart(Long cartId, Long productId, int quantity) {
        try {
            Cart cart = cartService.getCartById(cartId);  // Obtener el carrito directamente aquí
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

            if (product.getStock() < quantity) {
                throw new RuntimeException("Not enough stock for product with id " + productId);
            }

            CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

            if (item == null) {
                item = new CartItem();
                item.setProduct(product);
                item.setQuantity(quantity);
                cart.getItems().add(item);
            } else {
                item.setQuantity(item.getQuantity() + quantity);
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            cartRepository.save(cart);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while adding product to cart.", e);
        }
    }

}
