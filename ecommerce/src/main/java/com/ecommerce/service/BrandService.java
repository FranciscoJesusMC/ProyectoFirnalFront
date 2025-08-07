package com.ecommerce.service;

import com.ecommerce.dto.request.BrandRequestDTO;
import com.ecommerce.dto.response.BrandResponseDTO;

import java.util.List;

public interface BrandService {

    List<BrandResponseDTO> getAllBrands();

    BrandResponseDTO getBrandById(Long id);

    BrandResponseDTO createBrand(BrandRequestDTO brandRequestDTO);

    BrandResponseDTO updateBrand(BrandRequestDTO brandRequestDTO,Long id);

    void deleteBrand(Long id);
}
