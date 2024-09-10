package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.Entity.CartItem;

import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, List<CartItem> cartItems); 
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
}
