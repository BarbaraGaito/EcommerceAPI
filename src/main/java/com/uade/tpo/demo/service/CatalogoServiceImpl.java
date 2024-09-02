package com.uade.tpo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.Entity.Catalogo;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.repository.CatalogoRepository;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private CartService cartService;  

    @Autowired
    private ProductService productService; 

    @Override
    public Catalogo createCatalogo(Catalogo catalogo) {
        return catalogoRepository.save(catalogo);
    }

    @Override
    public List<Product> filterByCategory(String description) {
        return productService.getAllProducts().stream()
                .filter(product -> product.getCategory() != null && 
                                   product.getCategory().getDescription().equalsIgnoreCase(description))
                .collect(Collectors.toList());
    }

    @Override
    public void addToCart(Long carritoId, Long productId, int quantity) {
        cartService.addProductToCart(carritoId, productId, quantity);
    }

    @Override
    public List<Product> getAllProductsFromCatalog() {
        return productService.getAllProducts();
    }
}
