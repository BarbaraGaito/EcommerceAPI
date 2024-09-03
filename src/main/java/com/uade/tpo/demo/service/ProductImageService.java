package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.ProductImage;
import org.springframework.stereotype.Service;

@Service
public interface ProductImageService {
    public ProductImage create(ProductImage image);

    public ProductImage viewById(long id);
}
