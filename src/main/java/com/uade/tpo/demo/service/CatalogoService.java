
package com.uade.tpo.demo.service;

import java.util.List;

import com.uade.tpo.demo.Entity.Catalogo;
import com.uade.tpo.demo.Entity.Product;

public interface CatalogoService {
    Catalogo createCatalogo(Catalogo catalogo);
    List<Product> filterByCategory(String description);
    void addToCart(Long carritoId, Long productId, int quantity);
    List<Product> getAllProductsFromCatalog();
}
