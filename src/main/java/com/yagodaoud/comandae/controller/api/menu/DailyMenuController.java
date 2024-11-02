package com.yagodaoud.comandae.controller.api.menu;

import com.yagodaoud.comandae.controller.api.ControllerInterface;
import com.yagodaoud.comandae.dto.menu.DailyMenuDTO;
import com.yagodaoud.comandae.model.menu.DailyMenu;
import com.yagodaoud.comandae.service.menu.DailyMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-menus")
public class DailyMenuController implements ControllerInterface<DailyMenu, DailyMenuDTO> {

    @Autowired
    private DailyMenuService dailyMenuService;

    @Override
    @GetMapping
    public List<DailyMenu> getAll(@RequestParam Boolean isDeleted) {
        return dailyMenuService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DailyMenu> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(dailyMenuService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<DailyMenu> create(@RequestBody DailyMenuDTO dailyMenuDTO) {
        return ResponseEntity.ok(dailyMenuService.create(dailyMenuDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<DailyMenu> update(@PathVariable Long id, @RequestBody DailyMenuDTO dailyMenuDTO) {
        return ResponseEntity.ok(dailyMenuService.update(id, dailyMenuDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        dailyMenuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
