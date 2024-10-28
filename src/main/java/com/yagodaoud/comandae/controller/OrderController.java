package com.yagodaoud.comandae.controller;

import com.yagodaoud.comandae.dto.OrderDTO;
import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements ControllerInterface<Order, OrderDTO>{

    @Autowired
    private OrderService orderService;

    @Override
    @GetMapping
    public List<Order> getAll(@RequestParam Boolean isDeleted) {
        return orderService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderDTO orderDTO) {

        return ResponseEntity.ok(orderService.create(orderDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order orderDetails) {
        return ResponseEntity.ok(orderService.update(id, orderDetails));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        orderService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
