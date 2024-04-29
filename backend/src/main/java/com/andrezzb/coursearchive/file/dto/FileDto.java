package com.andrezzb.coursearchive.file.dto;

import org.springframework.core.io.Resource;
import com.andrezzb.coursearchive.file.models.MaterialFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {
  private MaterialFile materialFile;
  private Resource fileResource;
}
