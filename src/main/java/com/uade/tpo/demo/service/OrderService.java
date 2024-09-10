package com.uade.tpo.demo.service;
 
import java.util.List;
import com.uade.tpo.demo.Entity.Order;
 
public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
}
 