package com.example.sneaker_hub_backend.controller;

import com.example.sneaker_hub_backend.service.ProfilePicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/update-profile-pic")
public class ProfilePicController {

    @Autowired
    private ProfilePicService profilePicService;

    @PostMapping("/{username}")
    public ResponseEntity<?> uploadProfilePic(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = profilePicService.uploadProfilePic(username, file);
            return ResponseEntity.ok().body(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
