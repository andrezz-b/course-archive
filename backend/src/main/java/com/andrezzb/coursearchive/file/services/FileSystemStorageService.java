package com.andrezzb.coursearchive.file.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
  private final Path rootLocation;

  public FileSystemStorageService() {
    this.rootLocation = Paths.get("uploads");
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize storage location", e);
    }
  }

  @Override
public void store(MultipartFile file, String filePath) {
    try {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file " + file.getOriginalFilename());
        }
        Path destinationDir = this.rootLocation.resolve(filePath).normalize().toAbsolutePath();
        if (!destinationDir.startsWith(this.rootLocation.toAbsolutePath())) {
            throw new RuntimeException(
                "Cannot store file outside current directory.");
        }
        Files.createDirectories(destinationDir); // Create the directory if it does not exist
        Path destinationFile = destinationDir.resolve(file.getOriginalFilename()); // Add the file name to the path
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                StandardCopyOption.REPLACE_EXISTING);
        }
    } catch (IOException e) {
        throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
    }
}

  @Override
  public Resource loadAsResource(String filePath) {
    try {
      Path file = rootLocation.resolve(filePath);
      Resource resource = new FileSystemResource(file);
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read file: " + filePath);
      }
    } catch (Exception e) {
      throw new RuntimeException("Could not read file: " + filePath, e);
    }
  }
}
