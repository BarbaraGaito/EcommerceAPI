package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.CartItem;
import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;  

    @Override
    public Order createOrder(Long userId, List<CartItem> cartItems) {        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        Order order = new Order();
        order.setUser(user);  

        double totalPrice = 0;
        double finalPrice = 0;
        for (CartItem item : cartItems) {
            double productPrice = item.getProduct().getPrice();
            double discount = item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0;
            totalPrice += productPrice * item.getQuantity();
            finalPrice += (productPrice - (productPrice * discount / 100)) * item.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        order.setFinalPrice(finalPrice);
        
        order.setItems(cartItems);

        return orderRepository.save(order);
    }
    

    @Override
public Order updateOrder(Long id, Order orderDetails) {
    
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

    // Si los productos del carrito se actualizan
    if (orderDetails.getItems() != null && !orderDetails.getItems().isEmpty()) {
        existingOrder.setItems(orderDetails.getItems());
    }

    // Recalcular precios por cambio en productos
    double totalPrice = 0;
    double finalPrice = 0;
    for (CartItem item : existingOrder.getItems()) {
        double productPrice = item.getProduct().getPrice();
        double discount = item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0;
        totalPrice += productPrice * item.getQuantity();
        finalPrice += (productPrice - (productPrice * discount / 100)) * item.getQuantity();
    }
    existingOrder.setTotalPrice(totalPrice);
    existingOrder.setFinalPrice(finalPrice);

    return orderRepository.save(existingOrder);
}

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
