package com.ecommerce.controller;

import com.ecommerce.dto.PhotoDTO;
import com.ecommerce.serviceImpl.PhotoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200"})
public class PhotoController {

    private final PhotoServiceImpl photoService;

    @GetMapping("/product/{id}/photos")
    public ResponseEntity<List<PhotoDTO>> getPhotosByProductId(@PathVariable(name = "id")Long id){
        List<PhotoDTO> photoDTOS = photoService.getPhotosByProductId(id);
        return ResponseEntity.ok(photoDTOS);
    }

    @PostMapping("/product/{id}/uploadPhoto")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PhotoDTO>> uploadPhoto(@PathVariable(name = "id")Long id, List<MultipartFile> files ) throws IOException{
        List<PhotoDTO> photoDTOS =photoService.uploadPhotos(files,id);
        return ResponseEntity.ok(photoDTOS);
    }

    @DeleteMapping("/product/{productId}/photos/{photoId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePhotoFromProduct(@PathVariable(name = "productId")Long productId,@PathVariable(name = "photoId")Long photoId) throws IOException{
        photoService.deletePhoto(productId,photoId);
        return ResponseEntity.noContent().build();
    }
}
