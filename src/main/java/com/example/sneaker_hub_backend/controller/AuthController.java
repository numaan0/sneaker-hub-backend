package com.example.sneaker_hub_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.service.AppUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login")
    public AppUser login(@RequestBody AppUser user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (AppUser) authentication.getPrincipal();
        }
        return null;
    }

    @PostMapping("/signup")
    public AppUser signup(@RequestBody AppUser user) {
        return appUserService.save(user);
    }
}
