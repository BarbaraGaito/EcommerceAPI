package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.Entity.Catalogo;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
}
