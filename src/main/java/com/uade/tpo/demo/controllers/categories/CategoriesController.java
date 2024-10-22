package com.uade.tpo.demo.controllers.categories;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.Category;
import com.uade.tpo.demo.Entity.dto.CategoryDTO;
import com.uade.tpo.demo.Entity.dto.CategoryRequest;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping
public ResponseEntity<List<CategoryDTO>> getCategories() {
    List<Category> categories = categoryService.getCategories();
    List<CategoryDTO> categoryDTOs = categories.stream()
                                               .map(c -> new CategoryDTO(c.getId(), c.getDescription()))
                                               .collect(Collectors.toList());
    return ResponseEntity.ok(categoryDTOs);
}

@GetMapping("/admin")
public ResponseEntity<List<CategoryDTO>> getCategoriesAdmin() {
    List<Category> categories = categoryService.getCategories();
    List<CategoryDTO> categoryDTOs = categories.stream()
                                               .map(c -> new CategoryDTO(c.getId(), c.getDescription()))
                                               .collect(Collectors.toList());
    return ResponseEntity.ok(categoryDTOs);
}
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryRequest.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId())).body(result);
    }
}
