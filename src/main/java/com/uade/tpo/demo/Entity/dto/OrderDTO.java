package com.uade.tpo.demo.Entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private List<OrderItemDTO> items;
    private Double totalPrice;
    private UserDTO user;
}
