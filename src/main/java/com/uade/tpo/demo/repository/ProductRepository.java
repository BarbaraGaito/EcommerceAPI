package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
