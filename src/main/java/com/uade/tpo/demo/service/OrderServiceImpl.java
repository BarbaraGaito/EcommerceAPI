package com.uade.tpo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Order;
import com.uade.tpo.demo.Entity.OrderItem;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.Entity.dto.OrderDTO;
import com.uade.tpo.demo.Entity.dto.OrderItemDTO;
import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.Entity.dto.UserDTO;
import com.uade.tpo.demo.Entity.User;
import com.uade.tpo.demo.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.uade.tpo.demo.Entity.Role;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService; // Assuming you have a UserService to handle User entities

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Verificar si el usuario es ADMIN
        if (!isUserAdmin()) {
            throw new RuntimeException("Access denied: User is not authorized to create orders.");
        }

        Order order = new Order();
        order.setTotalPrice(orderDTO.getTotalPrice());
        User user = userService.getUserById(orderDTO.getUser().getId());
        order.setUser(user);

        List<OrderItem> orderItems = orderDTO.getItems().stream()
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
        return convertToOrderDTO(savedOrder);
    }

    @Override
        public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream() // Asegúrate de tener este método en el repositorio
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }


    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        return convertToOrderDTO(order);
    }

    private boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ADMIN.name()));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        // Verificar si el usuario es ADMIN
        if (!isUserAdmin()) {
            throw new RuntimeException("Access denied: User is not authorized to view orders.");
        }

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
                            item.getProduct().getCategory().getDescription())) // Asumiendo que tienes la descripción de la categoría
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
