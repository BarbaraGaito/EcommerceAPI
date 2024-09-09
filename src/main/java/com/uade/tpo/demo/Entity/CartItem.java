package com.uade.tpo.demo.Entity;
 
import jakarta.persistence.*;
import lombok.Data;
 
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
 
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cartItems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
 
    @Column(nullable = false)
<<<<<<< HEAD
    private Integer quantity = 0;
=======
    private Integer quantity=0;
>>>>>>> main
}