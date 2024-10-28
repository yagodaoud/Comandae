package com.yagodaoud.comandae.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ControllerInterface<T, R> {
    List<T> getAll(Boolean isDeleted);
    ResponseEntity<T> getById(Long id);
    ResponseEntity<T> create(R entity);
    ResponseEntity<T> update(Long id, T entity);
    ResponseEntity<Object> delete(Long id);
}
