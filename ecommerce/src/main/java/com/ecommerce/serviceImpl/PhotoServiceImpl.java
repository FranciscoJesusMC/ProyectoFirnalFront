package com.ecommerce.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.dto.PhotoDTO;
import com.ecommerce.entity.Photo;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.PhotoMapper;
import com.ecommerce.repository.PhotoRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper mapper;
    private final Cloudinary cloudinary;
    private final ProductRepository productRepository;

    @Override
    public List<PhotoDTO> getPhotosByProductId(Long productId) {
        List<Photo> photos = photoRepository.findByProductId(productId);
        if(photos.isEmpty()){
            throw new ResourceNotFoundException("The product photo list is empty");
        }
        return photos.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<PhotoDTO> uploadPhotos(List<MultipartFile> multipartFiles, Long id) throws IOException {
        log.info("Attempt to upload photo to the product with id : {}",id);

        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id:" + id));

        List<Photo> photos = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles){
            log.info("Uploading photo : {}",multipartFile.getOriginalFilename());

            try {
                Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
                Photo photo = new Photo();
                photo.setPublicId(uploadResult.get("public_id").toString());
                photo.setSecureUrl(uploadResult.get("secure_url").toString());
                photo.setProduct(product);
                photo = photoRepository.save(photo);
                photos.add(photo);

                log.info("Photo uploaded successfully : {}",photo.getPublicId());

            }catch (IOException e){
                log.error("Photo upload error : {}",multipartFile.getOriginalFilename());
                throw new IOException("Failed to upload photo: " + multipartFile.getOriginalFilename(), e);

            }
        }

        product.getPhotos().addAll(photos);
        productRepository.save(product);
        log.info("{} photos have been uploaded to the product with id: {}",photos.size(),id);

        return photos.stream().map(mapper::toDTO).toList();
    }

    @Override
    public void deletePhoto(Long id, Long photoId) throws IOException {
        log.info("Attempt to delete photo with id: {}",photoId);

        Photo photo = photoRepository.findById(photoId).orElseThrow(()->new ResourceNotFoundException("Photo not found with id :" + photoId));
        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id:" + id));

        if (!product.getPhotos().contains(photo)){
            log.warn("Attempt to delete a photo that does not belong to the product : {}", product.getId());
            throw new ResourceNotFoundException("The photo does not belong to the product");
        }

        product.getPhotos().remove(photo);
        productRepository.save(product);
        log.info("Photo with id : {} has been deleted to the product with Id : {}",photoId,product.getId());


        String publicId = photo.getPublicId();

        try {
            cloudinary.uploader().destroy(publicId,ObjectUtils.emptyMap());
            log.info("Photo has been deleted from cloudinary with publicId : {}", publicId);

        }catch (IOException e){
            log.error("Error deleting photo from cloudinary with publicId : {}",publicId);
            throw new IOException("Failed to delete photo from cloudinary",e);
        }

        photoRepository.delete(photo);
        log.info("Photo with id : {} has been deleted",photoId);
    }
}
