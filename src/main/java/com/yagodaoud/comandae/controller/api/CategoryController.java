package com.yagodaoud.comandae.controller.api;

import com.yagodaoud.comandae.dto.CategoryDTO;
import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController implements ControllerInterface<Category, CategoryDTO>{

    @Autowired
    private CategoryService categoryService;

    @Override
    @GetMapping
    public List<Category> getAll(Boolean isDeleted) {
        return categoryService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Category> create(CategoryDTO entity) {
        return ResponseEntity.ok(categoryService.create(entity));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(Long id, CategoryDTO entity) {
        return ResponseEntity.ok(categoryService.update(id, entity));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
