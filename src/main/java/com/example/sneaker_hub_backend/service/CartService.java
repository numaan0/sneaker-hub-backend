package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.Cart;
import com.example.sneaker_hub_backend.model.CartItem;
import com.example.sneaker_hub_backend.model.Product;
import com.example.sneaker_hub_backend.repository.CartRepository;
import com.example.sneaker_hub_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem(cart, product, quantity, product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cart.getItems().add(cartItem);
        cart.setTotalAmount(cart.getTotalAmount().add(cartItem.getPrice()));

        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId);
        CartItem cartItem = cart.getItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst().orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.getItems().remove(cartItem);
        cart.setTotalAmount(cart.getTotalAmount().subtract(cartItem.getPrice()));

        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepository.save(cart);
    }
}
