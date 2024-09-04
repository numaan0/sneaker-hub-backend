package com.example.sneaker_hub_backend.controller;

import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.service.AppUserService;
import com.example.sneaker_hub_backend.util.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private JwtUtil jwtUtil;

@PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(@RequestBody AppUser user) {
    try {
        if (appUserService.validateUser(user.getUsername(), user.getPassword())) {
            UserDetails userDetails = appUserService.loadUserByUsername(user.getUsername());
            String token = jwtUtil.generateToken(userDetails);

            AppUser existingUser = appUserService.findByUsername(user.getUsername())
                                                .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", existingUser);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid credentials"));
    } catch (Exception e) {
        // Log the exception details
        System.out.print(e);
        System.out.print("error in login");
        // logger.error("Login error: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
    }
}


    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean isTaken = appUserService.isUsernameTaken(username);
        return ResponseEntity.ok(!isTaken);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AppUser user) {
        if (appUserService.isUsernameTaken(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }

        try {
            // Save new user to the database
            AppUser newUser = appUserService.save(user);
            // Return the newly created user in the response body
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            // Handle exceptions such as database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the user");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUser(@PathVariable String username) {
        return appUserService.findByUsername(username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/update-password/{username}")
public ResponseEntity<?> updatePassword(@PathVariable String username, @RequestBody Map<String, String> request) {
    try {
        // Extract old and new passwords from request body
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        // Check if the user exists and validate the old password
        AppUser existingUser = appUserService.findByUsername(username)
                                            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!appUserService.validateUser(username, oldPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
        }

        // Update the password
        existingUser.setPassword(newPassword);
        AppUser updatedUser = appUserService.save(existingUser);

        return ResponseEntity.ok(updatedUser);
    } catch (Exception e) {
        // Handle exceptions such as database errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the password");
    }
}
@PutMapping("/update-details/{username}")
public ResponseEntity<?> updateUserDetails(@PathVariable String username, @RequestBody Map<String, Object> userUpdates) {
    try {
        // Check if the user exists
        AppUser existingUser = appUserService.findByUsername(username)
                                            .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields conditionally based on request body
        if (userUpdates.containsKey("email")) {
            existingUser.setEmail((String) userUpdates.get("email"));
        }
        if (userUpdates.containsKey("dob")) {
            existingUser.setDob(LocalDate.parse((String) userUpdates.get("dob"))); // Adjust based on your date format
        }
        if (userUpdates.containsKey("profile_pic")) {
            existingUser.setProfilePic((String) userUpdates.get("profile_pic"));
        }
        if (userUpdates.containsKey("user_type")) {
            existingUser.setUserType((String) userUpdates.get("user_type"));
        }
        if (userUpdates.containsKey("mobile_number")) {
            existingUser.setMobileNumber((String) userUpdates.get("mobile_number"));
        }
        if (userUpdates.containsKey("firstName")) {
            existingUser.setFirstName((String) userUpdates.get("firstName"));
        }
        if (userUpdates.containsKey("lastName")) {
            existingUser.setLastName((String) userUpdates.get("lastName"));
        }
        if (userUpdates.containsKey("gender")) {
            existingUser.setGender((String) userUpdates.get("gender"));
        }

        // Save updated user
        AppUser updatedUser = appUserService.updateuser(existingUser);

        return ResponseEntity.ok(updatedUser);
    } catch (Exception e) {
        // Handle exceptions such as database errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user details");
    }
}




}
