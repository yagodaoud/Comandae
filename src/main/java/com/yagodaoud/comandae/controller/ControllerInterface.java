package com.yagodaoud.comandae.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ControllerInterface<T> {
    List<T> getAll(String isDeleted);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<T> create(T entity);
    ResponseEntity<T> update(Long id, T entity);
    ResponseEntity<Object> delete(Long id);
}
