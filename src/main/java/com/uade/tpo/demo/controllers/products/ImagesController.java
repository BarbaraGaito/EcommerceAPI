package com.uade.tpo.demo.controllers.products;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.Entity.ProductImage;
import com.uade.tpo.demo.service.ProductImageService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uade.tpo.demo.Entity.dto.AddFileRequest;
import com.uade.tpo.demo.Entity.dto.ImageResponse;

@RestController
@RequestMapping("images")
public class ImagesController {
    @Autowired
    private ProductImageService imageService;

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<ImageResponse> displayImage(@RequestParam("id") long id) throws IOException, SQLException {
        ProductImage image = imageService.viewById(id);
        String encodedString = Base64.getEncoder()
                .encodeToString(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().body(ImageResponse.builder().file(encodedString).id(id).build());
    }

    @PostMapping()
    public String addImagePost(AddFileRequest request) throws IOException, SerialException, SQLException {
        byte[] bytes = request.getFile().getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        imageService.create(ProductImage.builder().image(blob).build());
        return "created";
    }
}
