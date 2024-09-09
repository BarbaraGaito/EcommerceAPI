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
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock for product with id " + productId);
        }

        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new CartItem());

        item.setProduct(product);
        item.setQuantity(item.getQuantity() + quantity);

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        cart.getItems().add(item);
        return cartRepository.save(cart);
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
        product.setStock(product.getStock() - quantityDifference);
        productRepository.save(product);

        return cartRepository.save(cart);
    }
    @Override
    public Double finishCart (Long cartId) {
        // Obtener el carrito por su ID
        Cart cart = getCartById(cartId);
        double total = 0;
 
    // Recorrer los items del carrito
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
 
            // Verificar si hay suficiente stock para cada producto
            if (product.getStock() < quantity) {
                throw new RuntimeException("Not enough stock for product with id " + product.getId());
            }
 
         // Calcular el precio del producto con descuento (si existe)
            double productPrice = product.getPrice();
            if (product.getDiscount() != null && product.getDiscount() > 0) {
                productPrice = productPrice - (productPrice * product.getDiscount() / 100);
            }
 
            // Sumar el costo total de este item al total del carrito
            total += productPrice * quantity;
 
            // Actualizar el stock del producto
            if (product.getStock() - quantity >= 0) {
               product.setStock(product.getStock() - quantity);
              productRepository.save(product);
         } else {
               throw new RuntimeException("Not enough stock for product with id " + product.getId());
            }
    }
 
        // Vaciar el carrito después de finalizar la compra
        cart.getItems().clear();
        cartRepository.save(cart);
 
        // Retornar el precio total del carrito
         return total;
}

}
