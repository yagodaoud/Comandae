package com.yagodaoud.comandae.controller.api.menu;

import com.yagodaoud.comandae.controller.api.ControllerInterface;
import com.yagodaoud.comandae.dto.menu.MenuItemDTO;
import com.yagodaoud.comandae.model.menu.MenuItem;
import com.yagodaoud.comandae.service.menu.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController implements ControllerInterface<MenuItem, MenuItemDTO> {

    @Autowired
    private MenuItemService menuItemService;

    @Override
    @GetMapping
    public List<MenuItem> getAll(@RequestParam Boolean isDeleted) {
        return menuItemService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(menuItemService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<MenuItem> create(@RequestBody MenuItemDTO menuItemDTO) {
        return ResponseEntity.ok(menuItemService.create(menuItemDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> update(@PathVariable Long id, @RequestBody MenuItemDTO menuItemDTO) {
        return ResponseEntity.ok(menuItemService.update(id, menuItemDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
