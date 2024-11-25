package com.uade.tpo.demo.controllers.order;

import com.uade.tpo.demo.Entity.dto.OrderDTO;
import com.uade.tpo.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

        @GetMapping("/user/{userId}")
            public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
            List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
