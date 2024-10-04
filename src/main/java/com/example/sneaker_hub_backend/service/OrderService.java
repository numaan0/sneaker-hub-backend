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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private final AppUserService appUserService;

    @Autowired
    public OrderService(@Lazy AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Autowired
    private ProductService productService;

    

    @Autowired
    private AppUserRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(Long userId, Order order) {
        // Fetch the user placing the order
        Optional<AppUser> userOpt = appUserService.getAppUserById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
    
        // Set the user for the order
        order.setUser(userOpt.get());
    
        // Set the order reference for each OrderItem
        for (OrderItem item : order.getOrderItems()) {
            // Check if the seller and product exist
            if (item.getSeller() == null || item.getSeller().getId() == null) {
                throw new RuntimeException("Seller ID is missing or invalid");
            }
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new RuntimeException("Product ID is missing or invalid");
            }
    
            // Fetch the seller and product based on their IDs
            Optional<AppUser> sellerOpt = appUserService.getAppUserById(item.getSeller().getId());
            Optional<Product> productOpt = productService.getProductById(item.getProduct().getId());
    
            if (sellerOpt.isEmpty() || productOpt.isEmpty()) {
                throw new RuntimeException("Seller or product not found");
            }
    
            // Set seller, product, and order reference for the OrderItem
            item.setSeller(sellerOpt.get());
            item.setProduct(productOpt.get());
            item.setOrder(order);  // Set the order reference in each OrderItem
        }
    
        // Set initial order status and date
        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());
    
        // Save the order (this will also save the order items because of the cascade setting)
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
    public void deleteOrdersByUserId(Long userId) {
        orderRepository.deleteByUserId(userId); 
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll(); 
    }
}
