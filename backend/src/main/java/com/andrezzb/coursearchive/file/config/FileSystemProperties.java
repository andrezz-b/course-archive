package com.andrezzb.coursearchive.file.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties("storage")
@Data
public class FileSystemProperties {
  private String uploadDirectory;
  private Path resolvedUploadDirectory;

  @PostConstruct
  public void init() {
    String currentDir = System.getProperty("user.dir");
    if (currentDir.endsWith("backend")) {
      this.resolvedUploadDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize();
    } else {
      this.resolvedUploadDirectory = Paths.get("backend", uploadDirectory).toAbsolutePath().normalize();
    }
  }
}
