package com.yagodaoud.comandae.controller;

import com.yagodaoud.comandae.dto.CustomerDTO;
import com.yagodaoud.comandae.model.Customer;
import com.yagodaoud.comandae.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController implements ControllerInterface<Customer, CustomerDTO>{

    @Autowired
    private CustomerService customerService;

    @Override
    @GetMapping
    public List<Customer> getAll(@RequestParam Boolean isDeleted) {
        return customerService.getAll(isDeleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.create(customerDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return ResponseEntity.ok(customerService.update(id, customerDetails));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

