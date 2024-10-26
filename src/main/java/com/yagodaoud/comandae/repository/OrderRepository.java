package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
