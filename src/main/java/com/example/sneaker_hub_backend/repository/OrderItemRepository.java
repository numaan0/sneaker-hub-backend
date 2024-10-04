package com.example.sneaker_hub_backend.repository;

import com.example.sneaker_hub_backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;




public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom method to delete order items by product ID
    void deleteByProductId(Long productId);
}