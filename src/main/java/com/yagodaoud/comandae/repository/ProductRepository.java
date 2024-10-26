package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
