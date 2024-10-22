package com.uade.tpo.demo.controllers.cart;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 
@CrossOrigin(origins = "http://localhost:5173")
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
 
    @GetMapping("/getAll")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }
 
    @PutMapping("/remove-product")
    public ResponseEntity<Void> removeProductFromCart(
            @RequestParam Long cartId,
            @RequestParam Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
 
    @PutMapping("/update-product-quantity")
    public ResponseEntity<Void> updateProductQuantityInCart(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            // Llamar al servicio para actualizar la cantidad del producto
            cartService.updateProductQuantityInCart(cartId, productId, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de errores en caso de que algo salga mal
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/incOne")
    public ResponseEntity<Void> incOne(@RequestParam Long cartId,
    @RequestParam Long productId) {
        try {
            // Llamar al servicio para actualizar la cantidad del producto
            cartService.incOne(cartId, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de errores en caso de que algo salga mal
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    @PutMapping("/decOne")
    public ResponseEntity<Void> decOne(@RequestParam Long cartId,
    @RequestParam Long productId) {
        try {
            // Llamar al servicio para actualizar la cantidad del producto
            cartService.decOne(cartId, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de errores en caso de que algo salga mal
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    @PutMapping("/{cartId}/clear")
public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
    try {
        cartService.clearCart(cartId); // Implementar lógica de vaciar el carrito en el servicio
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
 
 
    @PutMapping("/{cartId}/finish")
public ResponseEntity<Double> finishCart(@PathVariable Long cartId) {
    try {
        Double totalPrice = cartService.finishCart(cartId);  // This would still return the total price
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

 
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        try {
            CartDTO cartDTO = cartService.getCartByUserId(userId);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
 
}
 