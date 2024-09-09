package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();
            updatedOrder.setCount(order.getCount());
            updatedOrder.setUser(order.getUser());
            return orderRepository.save(updatedOrder);
        } else {
            throw new RuntimeException("Order not found with id " + id);
        }
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
