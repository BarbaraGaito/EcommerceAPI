
package com.uade.tpo.demo.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "catalogo")
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo_id")
    private List<Product> products = new ArrayList<>();

    public Catalogo() {
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
