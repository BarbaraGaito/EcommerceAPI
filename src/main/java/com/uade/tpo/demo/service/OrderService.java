package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.Entity.dto.OrderDTO;

public interface OrderService {
    Order createOrder(Order orderDTO);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getAllOrders();
    void deleteOrder(Long id);
    List<OrderDTO> getOrdersByUserId(Long userId);

}
