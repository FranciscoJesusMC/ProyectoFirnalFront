package com.ecommerce.mapper;

import com.ecommerce.dto.response.OrderProductResponseDTO;
import com.ecommerce.dto.response.OrderResponseDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {ProductMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "user", target = "userResponseDTO")
    OrderResponseDTO toDTO(Order order);

    @Mapping(source = "product", target = "productResponseDTO")
    OrderProductResponseDTO toOrderProductDTO(OrderProduct orderProduct);
}
