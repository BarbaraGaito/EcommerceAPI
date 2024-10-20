package com.uade.tpo.demo.Entity.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    private ProductDTO product;
    private Integer quantity;
}
