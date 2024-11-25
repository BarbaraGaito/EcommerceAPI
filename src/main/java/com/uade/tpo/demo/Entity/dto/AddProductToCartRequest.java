package com.uade.tpo.demo.Entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToCartRequest {
    private Long productId;
    private int quantity;
}