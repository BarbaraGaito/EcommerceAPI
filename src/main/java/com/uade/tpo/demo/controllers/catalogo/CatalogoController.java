package com.uade.tpo.demo.controllers.catalogo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.Catalogo;
import com.uade.tpo.demo.Entity.Product;
import com.uade.tpo.demo.service.CatalogoService;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public Catalogo createCatalogo(@RequestBody Catalogo catalogo) {
        return catalogoService.createCatalogo(catalogo);
    }

    // Método para obtener todos los productos
    @GetMapping("/productos")
    public List<Product> getAllProducts() {
        return (List<Product>) catalogoService.listarProductosJSON();
    }

    // Método para obtener el detalle de un producto por su ID
    @GetMapping("/productos/{productId}")
    public Product getProductDetail(@PathVariable Long productId) {
        return catalogoService.getProductDetail(productId);
    }

    // Método para filtrar productos por categoría
    @GetMapping("/filtrar")
    public List<Product> filterByCategory(@RequestParam String category) {
        return catalogoService.filterByCategory(category);
    }

    // Método para agregar productos al carrito
    @PostMapping("/{carritoId}/productos/{productId}/agregarCarrito")
    public void addToCarrito(@PathVariable Long carritoId, @PathVariable Long productId, @RequestParam int quantity) {
        catalogoService.addToCarrito(carritoId, productId, quantity);
    }
}
