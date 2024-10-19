package com.uade.tpo.demo.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.uade.tpo.demo.Entity.Cart;
 
import java.util.Optional;
 
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}