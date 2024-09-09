package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
}
