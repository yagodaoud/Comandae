package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.Bitcoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitcoinRepository extends JpaRepository<Bitcoin, Long> {
}
