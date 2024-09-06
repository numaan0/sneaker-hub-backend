package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.Cart;
import com.example.sneaker_hub_backend.model.CartItem;
import com.example.sneaker_hub_backend.model.Product;
import com.example.sneaker_hub_backend.repository.CartRepository;
import com.example.sneaker_hub_backend.repository.ProductRepository;
import com.example.sneaker_hub_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            cart.setItems(new ArrayList<>());
            cart.setTotalAmount(BigDecimal.ZERO);
        }
    
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    
        // Check if the cart already contains the product
        CartItem existingCartItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null);
    
        if (existingCartItem != null) {
            // Update the quantity and price of the existing cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            existingCartItem.setPrice(existingCartItem.getPrice().add(product.getPrice().multiply(BigDecimal.valueOf(quantity))));
        } else {
            // Add a new cart item
            CartItem cartItem = new CartItem(cart, product, quantity, product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(cartItem);
        }
    
        // Update the total amount of the cart
        cart.setTotalAmount(cart.getItems().stream()
            .map(CartItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
    
        return cartRepository.save(cart);
    }
    

    public Cart removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        CartItem cartItem = cart.getItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst().orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.getItems().remove(cartItem);
        cart.setTotalAmount(cart.getTotalAmount().subtract(cartItem.getPrice()));

        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
    
        System.out.println("Clearing cart for user: " + userId); // Add logging
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
    
        cartRepository.save(cart);
        System.out.println("Cart cleared for user: " + userId); // Add logging
    }
    
    
}
