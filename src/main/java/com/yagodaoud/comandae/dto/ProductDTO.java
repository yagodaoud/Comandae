package com.yagodaoud.comandae.dto;

import com.yagodaoud.comandae.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    Long id;
    String name;
    Category category;
    BigDecimal price;
    Integer quantity;
    String image;
    Boolean hasCustomValue;
    Integer stockQuantity;
    Boolean hasInfiniteStock;
}
