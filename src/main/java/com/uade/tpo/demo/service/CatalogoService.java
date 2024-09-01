package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.*;
import com.uade.tpo.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    // Repositorio de productos
    @Autowired
    private ProductRepository productRepository;

    public Catalogo createCatalogo(Catalogo catalogo) {
        return catalogoRepository.save(catalogo);
    }

    @GetMapping("/")
    public String listarProductos() {
        return "productos";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Product> obtenerProductoPorId(@PathVariable("id") UUID id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/listar")
    @ResponseBody
    public Iterable<Product> listarProductosJSON() {
        return productRepository.findAll();
    }

    // Método modificado para buscar producto por ID sin considerar catálogo
    public Product getProductDetail(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    // Método modificado para filtrar productos por categoría sin considerar catálogo
    public List<Product> filterByCategory(String description) {
        return productRepository.findAll().stream()
                .filter(product -> product.getCategory() != null && 
                                   product.getCategory().getDescription().equalsIgnoreCase(description))
                .collect(Collectors.toList());
    }

    public void addToCarrito(Long carritoId, Long productId, int quantity) {
        Carrito carrito = carritoRepository.findById(carritoId).orElse(new Carrito());
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            carrito.addProduct(product, quantity);
            productRepository.save(product); // Guardar el producto actualizado
            carritoRepository.save(carrito); // Guardar el carrito actualizado
        } else {
            throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
        }
    }
}
