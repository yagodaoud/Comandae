package com.yagodaoud.comandae.dto;

import lombok.Data;

@Data
public class OrderProductDTO {
    private OrderDTO orderDTO;
    private ProductDTO product;
    private Integer quantity;
}
