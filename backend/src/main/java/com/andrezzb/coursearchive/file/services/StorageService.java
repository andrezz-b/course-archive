package com.andrezzb.coursearchive.file.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  void store(MultipartFile file, String filePath);
  void delete(String filePath);
  Resource loadAsResource(String filePath);
} 
