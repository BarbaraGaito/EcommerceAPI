package com.uade.tpo.demo.service;
 

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.CartItem;
import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.Entity.OrderItem;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.Entity.dto.CartDTO;
import com.uade.tpo.demo.Entity.dto.CartItemDTO;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.ProductRepository;
 
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OrderService orderService;
 
    @Autowired
    private CartRepository cartRepository;
 
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productoService;

 
 
 
    @Autowired
    private ProductRepository productRepository;
 
    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

 
    @Override
    public void updateCart(Long id, Cart cart) {
        Optional<Cart> existingCart = cartRepository.findById(id);
        if (existingCart.isPresent()) {
            Cart updatedCart = existingCart.get();
            updatedCart.setItems(cart.getItems());
            cartRepository.save(updatedCart);
        } else {
            throw new RuntimeException("Cart not found with id " + id);
        }
    }

    @Override
    public void updateProductQuantityInCart(Long cartId, Long productId, int quantity) {
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
    cartRepository.save(cart);
}

 
    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }
    @Override
    public CartDTO getCartByIdDTO(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id " + id));
   
        return new CartDTO(
                cart.getId(),
                cart.getUserId(), // Obtener el userId del carrito
                cart.getItems().stream()
                    .map(cartItem -> new CartItemDTO(
                        cartItem.getId(),
                        cartItem.getProduct() != null ? cartItem.getProduct().getId() : null,
                        cartItem.getQuantity()))
                    .collect(Collectors.toList())
        );
    }
   
 
    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cart not found with id " + id));
    }
 
   @Override
    public List<CartDTO> getAllCarts() {
                List<Cart> carts = cartRepository.findAll();
 
                return carts.stream()
                        .map(cart -> new CartDTO(
                            cart.getId(),
                            cart.getUserId(),
                            cart.getItems().stream()
                                .map(cartItem -> new CartItemDTO(
                                    cartItem.getId(),
                                    cartItem.getProduct() != null ? cartItem.getProduct().getId() : null,
                                    cartItem.getQuantity()))
                                .collect(Collectors.toList())))
                    .collect(Collectors.toList());
    }
 
 
 
    @Override
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
        CartItem itemToRemove = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
 
        cart.getItems().remove(itemToRemove);
        productRepository.save(itemToRemove.getProduct());
        cartRepository.save(cart);
 
    }
 
    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        cart.getItems().clear(); // Vacía los items del carrito
        cartRepository.save(cart); // Guarda el carrito vacío
    }
 
    @Override
    public CartItemDTO incOne(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
    
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
    
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
    
        if (product.getStock() <= item.getQuantity()) {
            throw new RuntimeException("Not enough stock for product with id " + productId);
        }
    
        // Incrementar la cantidad
        item.setQuantity(item.getQuantity() + 1);
    
        // Guardar los cambios en el carrito
        cartRepository.save(cart);
    
        // Convertir a CartItemDTO y devolver
        return new CartItemDTO(
            item.getId(),
            item.getProduct().getId(),
            item.getQuantity()
        );
    }
    

 
    @Override
    public CartItemDTO decOne(Long cartId, Long productId) {
        // Obtener el carrito por ID
        Cart cart = getCartById(cartId);
    
        // Buscar el CartItem en el carrito
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProduct() != null && i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
    
        // Verificar si el producto existe
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
    
        // Verificar que la cantidad no sea menor a 1
        if (item.getQuantity() <= 1) {
            throw new RuntimeException("Cannot decrement quantity below 1 for product with id " + productId);
        }
    
        // Decrementar la cantidad
        item.setQuantity(item.getQuantity() - 1);
    
        // Guardar los cambios en el carrito
        cartRepository.save(cart);
    
        // Convertir a CartItemDTO y devolver
        return new CartItemDTO(
            item.getId(),
            item.getProduct().getId(),
            item.getQuantity()
        );
    }
    
   
 
 
    @Override
    public Double finishCart(Long cartId) {
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
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }

        User user = userService.getUserById(cart.getUserId());

        List<OrderItem> orderItems = cart.getItems().stream()
            .map(cartItem -> {
                Product producto = productoService.getProductById(cartItem.getProduct().getId());
                OrderItem orderItem = OrderItem.builder()
                        .product(producto) 
                        .quantity(cartItem.getQuantity())
                        .build();
                return orderItem;
            })
            .collect(Collectors.toList());

        Order order = new Order();
        order.setTotalPrice(total);
        order.setUser(user);
        order.setItems(orderItems); 

        orderService.createOrder(order);

       

        return total;
    }

            
        
 
 
    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No cart found for user with id " + userId));
 
        return new CartDTO(
                cart.getId(),
                cart.getUserId(),
                cart.getItems().stream()
                        .map(cartItem -> new CartItemDTO(
                                cartItem.getId(),
                                cartItem.getProduct() != null ? cartItem.getProduct().getId() : null,
                                cartItem.getQuantity()))
                        .collect(Collectors.toList())
        );
    }
 
}