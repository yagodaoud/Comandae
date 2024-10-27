package com.yagodaoud.comandae.controller;

import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController implements ControllerInterface<Product>{

    @Autowired
    private ProductService productService;

    @Override
    @GetMapping
    public List<Product> getAll(@RequestParam String isDeleted) {
        return productService.getAll(Boolean.getBoolean(isDeleted));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product entity) {
        return ResponseEntity.ok(productService.create(entity));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product productDetails) {
        return ResponseEntity.ok(productService.update(id, productDetails));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        productService.delete(id);
       return  ResponseEntity.noContent().build();
    }
}
