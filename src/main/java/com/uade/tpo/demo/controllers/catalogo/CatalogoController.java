package com.uade.tpo.demo.controllers.catalogo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.service.CatalogoService;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsInCatalog() {
        List<Product> products = catalogoService.getAllProductsFromCatalog();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    
    @GetMapping("/products/filter")
    public ResponseEntity<List<Product>> filterProductsByCategory(@RequestParam String description) {
        List<Product> filteredProducts = catalogoService.filterByCategory(description);
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }
}
