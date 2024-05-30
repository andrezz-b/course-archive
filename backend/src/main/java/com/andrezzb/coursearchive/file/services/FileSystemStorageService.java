package com.andrezzb.coursearchive.file.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import com.andrezzb.coursearchive.file.config.FileSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.andrezzb.coursearchive.file.exceptions.StorageException;
import com.andrezzb.coursearchive.file.exceptions.StorageFileNotFoundException;

@Service
@EnableConfigurationProperties(FileSystemProperties.class)
@Slf4j
public class FileSystemStorageService implements StorageService {
  private final Path rootLocation;

  public FileSystemStorageService(FileSystemProperties properties) {
    this.rootLocation = properties.getResolvedUploadDirectory();
    try {
      Files.createDirectories(rootLocation);
      log.info("Storage location initialized at {}", rootLocation.toAbsolutePath());
      log.info("User dir {}", System.getProperty("user.dir"));
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage location", e);
    }
  }

  @Override
  public void store(MultipartFile file, String filePath) {
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
      }
      Path destinationDir = this.rootLocation.resolve(filePath).normalize().toAbsolutePath();
      if (!destinationDir.startsWith(this.rootLocation.toAbsolutePath())) {
        throw new StorageException(
            "Cannot store file outside current directory.");
      }
      Files.createDirectories(destinationDir); // Create the directory if it does not exist
      Path destinationFile = destinationDir.resolve(Objects.requireNonNull(file.getOriginalFilename())); // Add the file
                                                                                 // name to the path
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile,
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
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
        throw new StorageFileNotFoundException("Could not read file: " + filePath);
      }
    } catch (Exception e) {
      throw new StorageException("Could not read file: " + filePath, e);
    }
  }

  @Override
  public void delete(String filePath) {
    try {
      Path file = rootLocation.resolve(filePath);
      Files.delete(file);
    } catch (IOException e) {
      throw new StorageException("Failed to delete file: " + filePath, e);
    }
  }
}
