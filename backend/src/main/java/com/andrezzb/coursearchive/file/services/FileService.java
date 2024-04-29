package com.andrezzb.coursearchive.file.services;

import java.io.IOException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.andrezzb.coursearchive.file.dto.FileDto;
import com.andrezzb.coursearchive.file.models.MaterialFile;
import com.andrezzb.coursearchive.file.repository.FileRepository;
import com.andrezzb.coursearchive.material.models.Material;

@Service
public class FileService {
  private final StorageService storageService;
  private final FileRepository fileRepository;
  private final Tika tika;

  public FileService(StorageService storageService, FileRepository fileRepository) {
    this.storageService = storageService;
    this.fileRepository = fileRepository;
    this.tika = new Tika();
  }

  public FileDto retreiveMaterialFile(Material material) {
    MaterialFile materialFile = fileRepository.findByMaterial(material);
    if (materialFile == null) {
      throw new RuntimeException("Material file not found");
    }
    var resource = storageService.loadAsResource(materialFile.getPath());
    return FileDto.builder().materialFile(materialFile).fileResource(resource).build();
  }

  public void saveFile(MultipartFile file, Material material) {
    MaterialFile materialFile = new MaterialFile();
    materialFile.setName(file.getOriginalFilename());
    try {
      materialFile.setMimeType(getFileType(file));
    } catch (Exception e) {
      throw new RuntimeException("Failed to get file type", e);
    }
    String filePathPrefix = generateFilePathPrefix(material);
    materialFile.setPath(filePathPrefix + "/" + file.getOriginalFilename());
    materialFile.setMaterial(material);
    storageService.store(file, materialFile.getPath());

    fileRepository.save(materialFile);
  }

  private String generateFilePathPrefix(Material material) {
    return "material/" + material.getId();
  }

  private String getFileType(MultipartFile file) throws IOException {
    return tika.detect(file.getBytes());
  }
}
