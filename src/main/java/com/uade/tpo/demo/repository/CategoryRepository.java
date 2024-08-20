package com.uade.tpo.demo.repository;

import java.util.ArrayList;
import java.util.Arrays;

import com.uade.tpo.demo.Entity.Category;

public class CategoryRepository {
    private ArrayList<Category> categories = new ArrayList<Category>(
        Arrays.asList(Category.builder()
                                .description("Zapatillas")
                                .id(1)
                                .build(),
                        Category.builder()
                                .description("Accesorios")
                                .id(2)
                                .build()         )
    );
}
