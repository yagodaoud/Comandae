package com.yagodaoud.comandae.controller.api;

import com.yagodaoud.comandae.dto.BitcoinDTO;
import com.yagodaoud.comandae.model.Bitcoin;
import com.yagodaoud.comandae.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bitcoin")
public class BitcoinController implements ControllerInterface<Bitcoin, BitcoinDTO> {

    @Autowired
    private BitcoinService bitcoinService;

    @Override
    @GetMapping
    public List<Bitcoin> getAll(@RequestParam Boolean isDeleted) {
        return bitcoinService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Bitcoin> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bitcoinService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Bitcoin> create(@RequestBody BitcoinDTO bitcoinDTO) {
        return ResponseEntity.ok(bitcoinService.create(bitcoinDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Bitcoin> update(@PathVariable Long id, @RequestBody BitcoinDTO bitcoinDTO) {
        return ResponseEntity.ok(bitcoinService.update(id, bitcoinDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        bitcoinService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

