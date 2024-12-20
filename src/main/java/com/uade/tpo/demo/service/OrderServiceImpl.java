package com.uade.tpo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.Entity.OrderItem;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.Entity.dto.OrderDTO;
import com.uade.tpo.demo.Entity.dto.OrderItemDTO;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.Entity.dto.UserDTO;
import com.uade.tpo.demo.repository.OrderRepository;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService; 

    @Override
    public Order createOrder(Order order) {
        order.setTotalPrice(order.getTotalPrice());
        User user = userService.getUserById(order.getUser().getId());
        order.setUser(user);

        List<OrderItem> orderItems = order.getItems().stream()
                .map(itemDTO -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(itemDTO.getQuantity());
                    Product product = new Product();
                    product.setId(itemDTO.getProduct().getId());
                    orderItem.setProduct(product);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    @Override
        public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream() 
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }


    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        return convertToOrderDTO(order);
    }


    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToOrderDTO(Order order) {
    List<OrderItemDTO> orderItemsDTO = order.getItems().stream()
            .map(item -> OrderItemDTO.builder()
                    .id(item.getId())
                    .product(new ProductDTO(item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getProduct().getDescription(),
                            item.getProduct().getPrice(),
                            item.getProduct().getStock(),
                            item.getProduct().getDiscount(),
                            item.getProduct().getCategory().getDescription())) 
                    .quantity(item.getQuantity())
                    .build())
            .collect(Collectors.toList());

    return OrderDTO.builder()
            .id(order.getId())
            .totalPrice(order.getTotalPrice())
            .user(new UserDTO(order.getUser().getId(), order.getUser().getEmail(), order.getUser().getName()))
            .items(orderItemsDTO)
            .build();
}

}
