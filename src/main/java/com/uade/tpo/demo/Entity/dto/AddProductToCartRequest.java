package com.uade.tpo.demo.Entity.dto;


public class AddProductToCartRequest {
    private Long productId;
    private int quantity;
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getters y Setters
}