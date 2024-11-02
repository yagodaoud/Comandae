package com.yagodaoud.comandae.controller.api.menu;

import com.yagodaoud.comandae.controller.api.ControllerInterface;
import com.yagodaoud.comandae.dto.menu.MenuHeaderDTO;
import com.yagodaoud.comandae.model.menu.MenuHeader;
import com.yagodaoud.comandae.service.menu.MenuHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-headers")
public class MenuHeaderController implements ControllerInterface<MenuHeader, MenuHeaderDTO> {

    @Autowired
    private MenuHeaderService menuHeaderService;

    @Override
    @GetMapping
    public List<MenuHeader> getAll(@RequestParam Boolean isDeleted) {
        return menuHeaderService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MenuHeader> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(menuHeaderService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<MenuHeader> create(@RequestBody MenuHeaderDTO menuHeaderDTO) {
        return ResponseEntity.ok(menuHeaderService.create(menuHeaderDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MenuHeader> update(@PathVariable Long id, @RequestBody MenuHeaderDTO menuHeaderDTO) {
        return ResponseEntity.ok(menuHeaderService.update(id, menuHeaderDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        menuHeaderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}