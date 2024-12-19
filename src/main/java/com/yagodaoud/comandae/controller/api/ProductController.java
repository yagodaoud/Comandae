package com.yagodaoud.comandae.controller.api;

import com.yagodaoud.comandae.dto.ProductDTO;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController implements ControllerInterface<Product, ProductDTO>{

    @Autowired
    private ProductService productService;

    @Override
    @GetMapping
    public List<Product> getAll(@RequestParam Boolean isDeleted) {
        return productService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.create(productDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        productService.delete(id);
       return ResponseEntity.noContent().build();
    }
}
