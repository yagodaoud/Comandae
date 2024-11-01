package com.yagodaoud.comandae.controller.menu;

import com.yagodaoud.comandae.controller.ControllerInterface;
import com.yagodaoud.comandae.dto.menu.DailyMenuItemDTO;
import com.yagodaoud.comandae.model.menu.DailyMenuItem;
import com.yagodaoud.comandae.service.menu.MenuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu-categories")
public class DailyMenuItemController implements ControllerInterface<DailyMenuItem, DailyMenuItemDTO> {

    @Autowired
    private MenuCategoryService menuCategoryService;

    @Override
    public List<DailyMenuItem> getAll(Boolean isDeleted) {
        return List.of();
    }

    @Override
    public ResponseEntity<DailyMenuItem> getById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<DailyMenuItem> create(DailyMenuItemDTO entity) {
        return null;
    }

    @Override
    public ResponseEntity<DailyMenuItem> update(Long id, DailyMenuItemDTO entity) {
        return null;
    }

    @Override
    public ResponseEntity<Object> delete(Long id) {
        return null;
    }
}
