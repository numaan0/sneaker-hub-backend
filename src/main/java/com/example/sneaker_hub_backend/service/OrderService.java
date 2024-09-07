package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.Order;
import com.example.sneaker_hub_backend.model.OrderItem;
import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.model.OrderStatus;
import com.example.sneaker_hub_backend.model.Product;
import com.example.sneaker_hub_backend.repository.AppUserRepository;
import com.example.sneaker_hub_backend.repository.OrderRepository;
import com.example.sneaker_hub_backend.repository.ProductRepository;

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

    

    @Autowired
    private AppUserRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(Long userId, Order order) {
        Optional<AppUser> userOpt = appUserService.getAppUserById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        // Set the user for the order
        order.setUser(userOpt.get());
    
        // Set the order reference for each OrderItem
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }
    
        // Set the initial order status
        order.setStatus(OrderStatus.PLACED);
        
        // Save and return the order
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                // Fetch and set seller details
                AppUser seller = sellerRepository.findById(item.getSeller().getId()).orElse(null);
                item.setSeller(seller);

                // Fetch and set product details
                Product product = productRepository.findById(item.getProduct().getId()).orElse(null);
                item.setProduct(product);
            }
        }
        return orders;
    }

    public List<Order> getOrdersBySellerId(Long sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
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
