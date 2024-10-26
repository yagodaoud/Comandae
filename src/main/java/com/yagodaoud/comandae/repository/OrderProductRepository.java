package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
