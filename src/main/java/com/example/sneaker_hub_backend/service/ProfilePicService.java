package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfilePicService {

    @Value("${profile.pics.directory:src/main/resources/static/profilePic}")
    private String profilePicsDirectory;

    private final AppUserRepository userRepository;

    public ProfilePicService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String uploadProfilePic(String username, MultipartFile file) throws IOException {
        // Construct the directory path for the user's profile pictures
        Path userDirectory = Paths.get(profilePicsDirectory, username);

        // Create the directory if it does not exist
        if (!Files.exists(userDirectory)) {
            Files.createDirectories(userDirectory);
        }

        // Define the path to the profile picture file
        Path filePath = userDirectory.resolve("profilepic.jpg");

        // Delete the existing file if it exists
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Save the new file
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            throw new IOException("Error saving file: " + e.getMessage(), e);
        }

        // Generate the URL where the profile picture can be accessed
        String profilePicUrl = "/profilePic/" + username + "/profilepic.jpg";

        // Update the user's profile picture URL in the database
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePic(profilePicUrl);
        userRepository.save(user);

        return profilePicUrl;
    }
}
