package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.Cart;

public interface CartService {
    Cart createCart(Cart cart);
    Cart updateCart(Long id, Cart cart);
    void deleteCart(Long id);
    Cart getCartById(Long id);
    List<Cart> getAllCarts();
    Cart addProductToCart(Long cartId, Long productId, int quantity);
    Cart removeProductFromCart(Long cartId, Long productId);
    Cart updateProductQuantityInCart(Long cartId, Long productId, int quantity);
    Double finishCart(Long cartId);
}
