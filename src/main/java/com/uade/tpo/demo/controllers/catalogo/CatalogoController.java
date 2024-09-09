package com.uade.tpo.demo.controllers.catalogo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/products/by-category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> filterByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = catalogoService.filterByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/filter-by-price")
    public List<ProductDTO> filterByPrice(
        @RequestParam Double minPrice, 
        @RequestParam Double maxPrice) {
        return catalogoService.filterByPrice(minPrice, maxPrice);
    }

    
    @PutMapping("/{cartId}/add-product")
    public ResponseEntity<String> addProductToCart(@PathVariable Long cartId,
                                                   @RequestParam Long productId,
                                                   @RequestParam int quantity) {
        try {
            catalogoService.addProductToCart(cartId, productId, quantity);
            return new ResponseEntity<>("Product added to cart successfully.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while adding product to cart.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
