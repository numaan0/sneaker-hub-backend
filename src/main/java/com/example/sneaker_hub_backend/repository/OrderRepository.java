package com.example.sneaker_hub_backend.repository;

import com.example.sneaker_hub_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.seller.id = :sellerId")
    List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);
}
