package com.example.sneaker_hub_backend.repository;

import com.example.sneaker_hub_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySellerId(Long sellerId);  // Changed from findBySeller_id to findBySellerId
}