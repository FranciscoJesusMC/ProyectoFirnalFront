package com.ecommerce.mapper;

import com.ecommerce.dto.request.CategoryRequestDTO;
import com.ecommerce.dto.response.CategoryResponseDTO;
import com.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO toDTO(Category category);

}
