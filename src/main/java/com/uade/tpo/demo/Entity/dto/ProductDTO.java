package com.uade.tpo.demo.Entity.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private double discount;
    private String categoryDescription;
    private List<String> images; // Add this to store base64 images

    // Constructor
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
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public String getCategoryDescription() { return categoryDescription; }
    public void setCategoryDescription(String categoryDescription) { this.categoryDescription = categoryDescription; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}
