package com.yagodaoud.comandae.controller.api.menu;

import com.yagodaoud.comandae.controller.api.ControllerInterface;
import com.yagodaoud.comandae.dto.menu.MenuCategoryDTO;
import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.service.menu.MenuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-categories")
public class MenuCategoryController implements ControllerInterface<MenuCategory, MenuCategoryDTO> {

    @Autowired
    private MenuCategoryService menuCategoryService;

    @Override
    @GetMapping
    public List<MenuCategory> getAll(@RequestParam Boolean isDeleted) {
        return menuCategoryService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MenuCategory> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(menuCategoryService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<MenuCategory> create(@RequestBody MenuCategoryDTO menuCategoryDTO) {
        return ResponseEntity.ok(menuCategoryService.create(menuCategoryDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MenuCategory> update(@PathVariable Long id, @RequestBody MenuCategoryDTO menuCategoryDTO) {
        return ResponseEntity.ok(menuCategoryService.update(id, menuCategoryDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        menuCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}