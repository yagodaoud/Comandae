package com.yagodaoud.comandae.controller.menu;

import com.yagodaoud.comandae.controller.ControllerInterface;
import com.yagodaoud.comandae.dto.menu.MenuFooterDTO;
import com.yagodaoud.comandae.model.menu.MenuFooter;
import com.yagodaoud.comandae.service.menu.MenuFooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-footers")
public class MenuFooterController implements ControllerInterface<MenuFooter, MenuFooterDTO> {

    @Autowired
    private MenuFooterService menuFooterService;

    @Override
    @GetMapping
    public List<MenuFooter> getAll(@RequestParam Boolean isDeleted) {
        return menuFooterService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MenuFooter> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(menuFooterService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<MenuFooter> create(@RequestBody MenuFooterDTO menuFooterDTO) {
        return ResponseEntity.ok(menuFooterService.create(menuFooterDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MenuFooter> update(@PathVariable Long id, @RequestBody MenuFooterDTO menuFooterDTO) {
        return ResponseEntity.ok(menuFooterService.update(id, menuFooterDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        menuFooterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
