package com.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private Long id;
    private LocalDateTime createdAt;
    private double total;
    private String status;
    private UserResponseDTO userResponseDTO;
    private List<OrderProductResponseDTO> orderProducts;
}
