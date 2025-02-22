package com.yagodaoud.comandae.dto;

import com.yagodaoud.comandae.model.PixType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PixDTO {
    private Long id;
    private PixType type;
    private String key;
    private String companyName;
    private String city;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
