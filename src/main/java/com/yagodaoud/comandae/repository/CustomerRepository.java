package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
