package com.uade.tpo.demo.controllers.catalogo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.dto.ProductDTO;
import com.uade.tpo.demo.service.CatalogoService;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProductsFromCatalog() {
        List<ProductDTO> products = catalogoService.getAllProductsFromCatalog();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/{carritoId}/productos/{productId}/agregarCarrito")
    public ResponseEntity<String> addToCart(@PathVariable Long carritoId, @PathVariable Long productId, @RequestParam int quantity) {
        return catalogoService.addToCart(carritoId, productId, quantity);
    }

    @GetMapping("/products/by-category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> filterByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = catalogoService.filterByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
