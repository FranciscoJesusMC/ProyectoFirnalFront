package com.ecommerce.mapper;

import com.ecommerce.dto.PhotoDTO;
import com.ecommerce.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    Photo toEntity(PhotoDTO photoDTO);

    PhotoDTO toDTO(Photo photo);

}
