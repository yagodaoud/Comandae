package com.yagodaoud.comandae.service;

import java.util.List;

public interface ServiceInterface<T> {
    List<T> getAll(boolean isDeleted);
    T getById(Long id);
    T create(T entity);
    T update(Long id, T entity);
    void delete(Long id);
}
