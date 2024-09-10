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
        // Obtener el carrito de la base de datos
        Cart cart = cartService.getCartById(cartId);  
        
        // Obtener el producto de la base de datos
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        // Verificar si hay suficiente stock
        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock for product with id " + productId);
        }

        // Buscar si el producto ya está en el carrito
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProduct() != null && i.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null);

        // Si no está en el carrito, crear uno nuevo
        if (item == null) {
            item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setCart(cart);  // Asegurarse de establecer la relación entre CartItem y Cart
            cart.getItems().add(item);
        } else {
            // Si ya existe, solo actualiza la cantidad
            item.setQuantity(item.getQuantity() + quantity);
        }

        // Restar el stock del producto
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);  // Guardar los cambios del producto

        cartRepository.save(cart);  // Guardar los cambios del carrito
    } catch (Exception e) {
        throw new RuntimeException("An error occurred while adding product to cart.", e);
    }
}

}
