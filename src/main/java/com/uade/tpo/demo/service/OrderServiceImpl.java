package com.uade.tpo.demo.service;
 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.service.OrderService;
 
@Service
public class OrderServiceImpl implements OrderService {
 
    @Autowired
    private OrderRepository orderRepository;
 
    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
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
 
    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
 