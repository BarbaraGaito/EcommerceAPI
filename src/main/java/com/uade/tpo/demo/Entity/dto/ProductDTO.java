package com.uade.tpo.demo.Entity.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private double discount;
    private String categoryDescription;
    private List<String> images; 

    
    public ProductDTO(Long id, String name, String description, Double price, Integer stock, double discount, String categoryDescription, List<String> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
        this.categoryDescription = categoryDescription;
        this.images = images;
    }
    public ProductDTO(Long id, String name, String description, Double price, Integer stock, double discount, String categoryDescription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
        this.categoryDescription = categoryDescription;
        this.images = new ArrayList<String>();
    }
}
