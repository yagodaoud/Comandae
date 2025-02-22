package com.yagodaoud.comandae.dto;

import com.yagodaoud.comandae.model.BitcoinNetwork;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BitcoinDTO {
    private Long id;
    private BitcoinNetwork network;
    private String address;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}

