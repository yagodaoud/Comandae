package com.yagodaoud.comandae.dto;

import com.yagodaoud.comandae.model.Category;

import java.math.BigDecimal;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getHasCustomValue() {
        return hasCustomValue;
    }

    public void setHasCustomValue(Boolean hasCustomValue) {
        this.hasCustomValue = hasCustomValue;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getHasInfiniteStock() {
        return hasInfiniteStock;
    }

    public void setHasInfiniteStock(Boolean hasInfiniteStock) {
        this.hasInfiniteStock = hasInfiniteStock;
    }
}
