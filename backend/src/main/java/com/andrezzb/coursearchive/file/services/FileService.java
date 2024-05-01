package com.andrezzb.coursearchive.file.services;

import java.io.IOException;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.andrezzb.coursearchive.file.exceptions.MaterialFileNotFoundException;
import com.andrezzb.coursearchive.file.exceptions.StorageException;
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

  public MaterialFile findMaterialFileByMaterial(Material material) {
    return findMaterialFileByMaterialId(material.getId());
  }

  public MaterialFile findMaterialFileByMaterialId(Long materialId) {
    return fileRepository.findByMaterialId(materialId).orElseThrow(() -> new MaterialFileNotFoundException(materialId));
  }

  public Resource loadMaterialFileResource(MaterialFile materialFile) {
    return storageService.loadAsResource(materialFile.getPath());
  }

  public void deleteMaterialFile(MaterialFile materialFile) {
    storageService.delete(materialFile.getPath());
    fileRepository.delete(materialFile);
  }

  public void saveFile(MultipartFile file, Material material) {
    MaterialFile materialFile = new MaterialFile();
    materialFile.setName(file.getOriginalFilename());
    try {
      materialFile.setMimeType(getFileType(file));
    } catch (Exception e) {
      throw new StorageException("Failed to get file type", e);
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
