package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.Category;
import com.uade.tpo.demo.service.CategoryService;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    @GetMapping
    public String getCategories(){
        Category cat = new Category();
        String description = cat.getDescription();
        CategoryService service = new CategoryService();
        return service.getCategories();
    }

    @GetMapping("/{categoryId}")
    public String getCategoryById(@PathVariable String categoryId){
        CategoryService service = new CategoryService();
        return service.getCategoryById(categoryId);
    }

    @PostMapping
    public String createCategory(@RequestBody String categoryId){
        CategoryService service = new CategoryService();
        return service.createCategory(categoryId);
    }
}
