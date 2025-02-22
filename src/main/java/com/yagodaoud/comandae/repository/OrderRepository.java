package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderSlipIdAndActiveIsTrue(Integer orderSlipId);

    Optional<Order> findByOrderSlipIdAndActiveTrue(Integer orderSlipId);

    List<Order> findByActiveIsFalseOrderByCreatedAtDesc();

    List<Order> findByActiveIsTrueOrderByCreatedAtDesc();
}
