package com.uade.tpo.demo.service;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.CartItem;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.ProductRepository;
 
@Service
public class CartServiceImpl implements CartService {
 
    @Autowired
    private CartRepository cartRepository;
 
    @Autowired
    private ProductRepository productRepository;
 
    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }
 
    @Override
    public Cart updateCart(Long id, Cart cart) {
        Optional<Cart> existingCart = cartRepository.findById(id);
        if (existingCart.isPresent()) {
            Cart updatedCart = existingCart.get();
            updatedCart.setItems(cart.getItems());
            return cartRepository.save(updatedCart);
        } else {
            throw new RuntimeException("Cart not found with id " + id);
        }
    }
 
    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
 
    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cart not found with id " + id));
    }
 
    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
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
    
   
 
    @Override
    public Cart removeProductFromCart(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
        CartItem itemToRemove = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
 
        cart.getItems().remove(itemToRemove);
        productRepository.save(itemToRemove.getProduct());
        return cartRepository.save(cart);
 
    }
 
    @Override
    public Cart updateProductQuantityInCart(Long cartId, Long productId, int quantity) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
 
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
 
        int quantityDifference = quantity - item.getQuantity();
        if (product.getStock() < quantityDifference) {
            throw new RuntimeException("Not enough stock for product with id " + productId);
        }
        item.setQuantity(quantity);
        return cartRepository.save(cart);
    }
    @Override
    public Double finishCart (Long cartId) {
        
        Cart cart = getCartById(cartId);
        double total = 0;
 
    
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
 
            
            if (product.getStock() < quantity) {
                throw new RuntimeException("Not enough stock for product with id " + product.getId());
            }
 
        
            double productPrice = product.getPrice();
            if (product.getDiscount() != null && product.getDiscount() > 0) {
                productPrice = productPrice - (productPrice * product.getDiscount() / 100);
            }
 
            
            total += productPrice * quantity;
 
            
            if (product.getStock() - quantity >= 0) {
               product.setStock(product.getStock() - quantity);
              productRepository.save(product);
         } else {
               throw new RuntimeException("Not enough stock for product with id " + product.getId());
            }
    }
 
       
        cart.getItems().clear();
        cartRepository.save(cart);
 
       
         return total;
}

}


