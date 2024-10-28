package com.yagodaoud.comandae.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private Long id;
    private int orderSlipId;
    private CustomerDTO customer;
    private List<ProductDTO> products;
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderSlipId() {
        return orderSlipId;
    }

    public void setOrderSlipId(int orderSlipId) {
        this.orderSlipId = orderSlipId;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
