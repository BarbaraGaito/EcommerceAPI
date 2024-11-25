package com.uade.tpo.demo.service;
 
import java.util.List;
import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.dto.CartDTO;
import com.uade.tpo.demo.Entity.dto.CartItemDTO;
 
public interface CartService {
    
    Cart createCart(Cart cart);
    void updateCart(Long id, Cart cart);
    void deleteCart(Long id);
    CartDTO getCartByIdDTO(Long id);
    Cart getCartById(Long id);
    List<CartDTO> getAllCarts();
    void removeProductFromCart(Long cartId, Long productId);
    void updateProductQuantityInCart(Long cartId, Long productId, int quantity);
    Double finishCart(Long cartId);
    CartItemDTO incOne(Long cartId, Long productId);
    CartItemDTO decOne(Long cartId, Long productId);
    void clearCart(Long cartId);
 
    CartDTO getCartByUserId(Long userId);
}