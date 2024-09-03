package com.uade.tpo.demo.service;

import com.uade.tpo.demo.Entity.ProductImage;
import com.uade.tpo.demo.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Autowired
    private ProductImageRepository imageRepository;

    @Override
    public ProductImage create(ProductImage image) {
        return imageRepository.save(image);
    }

    @Override
    public ProductImage viewById(long id) {
        return imageRepository.findById(id).get();
    }
}
