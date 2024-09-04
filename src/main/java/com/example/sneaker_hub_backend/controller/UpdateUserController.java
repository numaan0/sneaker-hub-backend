package com.example.sneaker_hub_backend.controller;

import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UpdateUserController {

    @Autowired
    private AppUserService appUserService;

    @PutMapping("/{username}")
    public ResponseEntity<AppUser> updateUser(
        @PathVariable String username,
        @RequestBody AppUser updatedUserData
    ) {
        return appUserService.findByUsername(username)
            .map(existingUser -> {
                // Update user details
                existingUser.setFirstName(updatedUserData.getFirstName());
                existingUser.setLastName(updatedUserData.getLastName());
                existingUser.setEmail(updatedUserData.getEmail());
                existingUser.setMobileNumber(updatedUserData.getMobileNumber());
                existingUser.setGender(updatedUserData.getGender());
                appUserService.save(existingUser);

                return ResponseEntity.ok(existingUser);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return NOT_FOUND with no body
    }
}
