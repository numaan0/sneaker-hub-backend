package com.example.sneaker_hub_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private int stock;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private AppUser seller;  // Changed from seller_id to seller

    // No-argument constructor (required by JPA)
    public Product() {}

    // Parameterized constructor
    public Product(String name, String description, String category, BigDecimal price, String imageUrl, int stock, AppUser seller) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.seller = seller;  // Changed from seller_id to seller
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public AppUser getSeller() { return seller; }  // Changed from getSeller_id to getSeller
    public void setSeller(AppUser seller) { this.seller = seller; }  // Changed from setSeller_id to setSeller
}