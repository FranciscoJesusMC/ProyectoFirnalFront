package com.ecommerce.serviceImpl;

import com.ecommerce.dto.request.ProductRequestDTO;
import com.ecommerce.dto.response.ProductResponseDTO;
import com.ecommerce.entity.Brand;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.repository.BrandRepository;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper mapper;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()){
            throw new ResourceNotFoundException("The list of products is empty");
        }
        return products.stream().map(mapper::toDTO).toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id :" + id));
        log.info("Product loaded: {}", product.getName());
        log.info("Category: {}", product.getCategory().getName());
        log.info("Brand: {}", product.getBrand().getName());
        log.info("Photos: {}", product.getPhotos().size());


        return mapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        log.info("Attempt to register product with name :{}",productRequestDTO.getName());

        Product product = mapper.toEntity(productRequestDTO);

        Brand brand = brandRepository.findById(productRequestDTO.getBrandId()).orElseThrow(()-> new ResourceNotFoundException("Brand not found with id:" + productRequestDTO.getBrandId()));
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category not found with id :" + productRequestDTO.getCategoryId()));

        product.setBrand(brand);
        product.setCategory(category);
        productRepository.save(product);
        log.info("Product has been registered with name : {}",productRequestDTO.getName());


        return mapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(ProductRequestDTO productRequestDTO, Long id) {
        log.info("Attempt to update product with id: {}", id);

        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id:" + id));
        Brand brand = brandRepository.findById(productRequestDTO.getBrandId()).orElseThrow(()-> new ResourceNotFoundException("Brand not found with id:" + productRequestDTO.getBrandId()));
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category not found with id :" + productRequestDTO.getCategoryId()));

        product.setDescription(productRequestDTO.getDescription());
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setCategory(category);
        product.setBrand(brand);
        productRepository.save(product);
        log.info("Product has been updated with id : {}",id);

        return mapper.toDTO(product);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Attempt to delete product with id: {}",id);

        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found with id :" + id));

        productRepository.delete(product);
        log.info("Product has been deleted: {}",id);
    }
}
