package com.example.sneaker_hub_backend.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String productName;
    private int quantity;
    private BigDecimal price;
    private Long sellerId;
    private Long productId;

    // Constructors
    public OrderItemDTO() {}

    public OrderItemDTO(String productName, int quantity, BigDecimal price, Long sellerId, Long productId) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.sellerId = sellerId;
        this.productId = productId;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
