package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.Order;
import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.model.OrderStatus;
import com.example.sneaker_hub_backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppUserService appUserService;

    public Order createOrder(Long userId, Order order) {
        Optional<AppUser> userOpt = appUserService.getAppUserById(userId);
        if (userOpt == null) {
            throw new RuntimeException("User not found");
        }
        order.setUser(userOpt.get());
        order.setStatus(OrderStatus.PLACED);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }
}