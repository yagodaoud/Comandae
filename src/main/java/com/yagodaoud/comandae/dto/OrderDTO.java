package com.yagodaoud.comandae.dto;

import com.yagodaoud.comandae.model.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private int orderSlipId;
    private CustomerDTO customer;
    private List<ProductDTO> products;
    private BigDecimal total;
    private PaymentType paymentType;
}
