package com.yagodaoud.comandae.repository;

import com.yagodaoud.comandae.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixRepository extends JpaRepository<Pix, Long> {
    Pix findByIsActive(boolean isActive);
}