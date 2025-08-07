package com.ecommerce.service;

import com.ecommerce.dto.PhotoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    List<PhotoDTO> getPhotosByProductId(Long productId);

    List<PhotoDTO> uploadPhotos(List<MultipartFile> multipartFiles,Long id) throws IOException;

    void deletePhoto(Long id,Long photoId) throws IOException;
}
