package com.yagodaoud.comandae.controller.api;

import com.yagodaoud.comandae.dto.PixDTO;
import com.yagodaoud.comandae.model.Pix;
import com.yagodaoud.comandae.service.PixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pix")
public class PixController implements ControllerInterface<Pix, PixDTO> {

    @Autowired
    private PixService pixService;

    @Override
    @GetMapping
    public List<Pix> getAll(@RequestParam Boolean isDeleted) {
        return pixService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Pix> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pixService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Pix> create(@RequestBody PixDTO pixDTO) {
        return ResponseEntity.ok(pixService.create(pixDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Pix> update(@PathVariable Long id, @RequestBody PixDTO pixDTO) {
        return ResponseEntity.ok(pixService.update(id, pixDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        pixService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
