package com.yagodaoud.comandae.controller;

import com.yagodaoud.comandae.dto.OrderProductDTO;
import com.yagodaoud.comandae.model.OrderProduct;
import com.yagodaoud.comandae.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-products")
public class OrderProductController implements ControllerInterface<OrderProduct, OrderProductDTO>{

    @Autowired
    private OrderProductService orderProductService;

    @Override
    @GetMapping
    public List<OrderProduct> getAll(@RequestParam Boolean isDeleted) {
        return orderProductService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrderProduct> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderProductService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<OrderProduct> create(@RequestBody OrderProductDTO orderProductDTO) {
        return ResponseEntity.ok(orderProductService.create(orderProductDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<OrderProduct> update(@PathVariable Long id, @RequestBody OrderProduct orderProductDetails) {
        return ResponseEntity.ok(orderProductService.update(id, orderProductDetails));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        orderProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


