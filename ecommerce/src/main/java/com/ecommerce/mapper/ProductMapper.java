package com.ecommerce.mapper;


import com.ecommerce.dto.request.ProductRequestDTO;
import com.ecommerce.dto.response.ProductResponseDTO;
import com.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "category.id",source = "categoryId")
    @Mapping(target = "brand.id",source = "brandId")
    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(target = "categoryResponseDTO",source = "category")
    @Mapping(target = "brandResponseDTO",source = "brand")
    @Mapping(target = "photoDTOS",source = "photos")
    ProductResponseDTO toDTO(Product product);

}
