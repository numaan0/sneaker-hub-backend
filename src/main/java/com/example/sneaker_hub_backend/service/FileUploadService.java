package com.example.sneaker_hub_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Value("${file.upload.directory:src/main/resources/static}")
    private String uploadDirectory;

    public String uploadFile(String folder, String fileName, MultipartFile file) throws IOException {
        // Construct the directory path for the files
        Path folderPath = Paths.get(uploadDirectory, folder);

        // Create the directory if it does not exist
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // Define the path to the file
        Path filePath = folderPath.resolve(fileName);

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

        // Return the relative URL where the file can be accessed
        return "/" + folder + "/" + fileName;
    }
}
