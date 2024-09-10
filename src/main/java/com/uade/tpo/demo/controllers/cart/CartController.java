package com.uade.tpo.demo.controllers.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.Cart;
import com.uade.tpo.demo.Entity.dto.CartDTO;
import com.uade.tpo.demo.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
 
    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        Cart createdCart = cartService.createCart(cart);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long id, @RequestBody Cart cart) {
         cartService.updateCart(id, cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartByIdDTO(@PathVariable Long id) {
        CartDTO cart = cartService.getCartByIdDTO(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
 
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }
    
    @PutMapping("/{cartId}/add-product/{productId}/{quantity}")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @PathVariable int quantity) {
         cartService.addProductToCart(cartId, productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
   
    @PutMapping("/{cartId}/remove-product/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return new ResponseEntity<>( HttpStatus.OK);
    }
   
    @PutMapping("/{cartId}/update-product-quantity/{productId}/{quantity}")
    public ResponseEntity<Cart> updateProductQuantityInCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @PathVariable int quantity) {
        cartService.updateProductQuantityInCart(cartId, productId, quantity);
         return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/{cartId}/finish")
    public ResponseEntity<Double> finishCart(@PathVariable Long cartId) {
        // Llamar al servicio para finalizar el carrito y obtener el precio total
        Double totalPrice = cartService.finishCart(cartId);
       
        // Retornar el precio total en la respuesta
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    }
}

