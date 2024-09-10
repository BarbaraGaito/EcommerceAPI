package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.dto.CartDTO;

public interface CartService {
    Cart createCart(Cart cart);
    void updateCart(Long id, Cart cart);
    void deleteCart(Long id);
    CartDTO getCartByIdDTO(Long id);
    Cart getCartById(Long id);
    List<CartDTO> getAllCarts();
    void addProductToCart(Long cartId, Long productId, int quantity);
    void removeProductFromCart(Long cartId, Long productId);
    void updateProductQuantityInCart(Long cartId, Long productId, int quantity);
    Double finishCart(Long cartId);
    
}
