package com.uade.tpo.demo.service;
 

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.CartItem;

import java.util.ArrayList;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.Entity.dto.CartDTO;
import com.uade.tpo.demo.Entity.dto.CartItemDTO;
import com.uade.tpo.demo.Entity.dto.OrderDTO;
import com.uade.tpo.demo.Entity.dto.OrderItemDTO;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.Entity.dto.UserDTO;
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
    public void incOne(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
 
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
 
        if (product.getStock() < 1) {
            throw new RuntimeException("Not enough stock for product with id " + productId);
        }
        item.setQuantity(item.getQuantity()+1);
         cartRepository.save(cart);
        }
 
        @Override
        public void decOne(Long cartId, Long productId) {
            Cart cart = getCartById(cartId);
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
     
            CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart with id " + productId));
     
            if (product.getStock() < 1) {
                throw new RuntimeException("Not enough stock for product with id " + productId);
            }
            item.setQuantity(item.getQuantity()-1);
             cartRepository.save(cart);
            }
   
 
 
    @Override
    public Double finishCart(Long cartId) {
        Cart cart = getCartById(cartId);
        double total = 0;

        // Calcular el total
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

        // Obtener el usuario utilizando el userId del carrito
        User user = userService.getUserById(cart.getUserId());

        // Convertir CartItem a OrderItemDTO
        List<OrderItemDTO> orderItemsDTO = cart.getItems().stream()
            .map(cartItem -> {
                OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                        .product(new ProductDTO(
                                cartItem.getProduct().getId(),
                                cartItem.getProduct().getName(),
                                cartItem.getProduct().getDescription(),
                                cartItem.getProduct().getPrice(),
                                cartItem.getProduct().getStock(),
                                cartItem.getProduct().getDiscount(),
                                cartItem.getProduct().getCategory().getDescription())) // Aquí puedes obtener la descripción de la categoría
                        .quantity(cartItem.getQuantity())
                        .build();
                return orderItemDTO;
            })
            .collect(Collectors.toList());

        // Crear la nueva orden con OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(total);
        orderDTO.setUser(new UserDTO(user.getId(), user.getEmail(), user.getName()));
        orderDTO.setItems(orderItemsDTO); // Asignar la lista de OrderItemsDTO

        // Crear la orden
        orderService.createOrder(orderDTO);

        // Vaciar el carrito
        cart.getItems().clear();
        cartRepository.save(cart);

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