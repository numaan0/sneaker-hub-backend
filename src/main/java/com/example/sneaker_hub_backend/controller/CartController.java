package com.example.sneaker_hub_backend.controller;

import com.example.sneaker_hub_backend.model.Cart;
import com.example.sneaker_hub_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/add")
    public Cart addItemToCart(@PathVariable Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addItemToCart(userId, productId, quantity);
    }

    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public Cart removeItemFromCart(@PathVariable Long userId, @PathVariable Long cartItemId) {
        return cartService.removeItemFromCart(userId, cartItemId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
