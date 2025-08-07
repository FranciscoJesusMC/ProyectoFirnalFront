package com.ecommerce.mapper;

import com.ecommerce.dto.request.BrandRequestDTO;
import com.ecommerce.dto.response.BrandResponseDTO;
import com.ecommerce.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    Brand toEntity(BrandRequestDTO brandRequestDTO);

    BrandResponseDTO toDTO(Brand brand);
}
