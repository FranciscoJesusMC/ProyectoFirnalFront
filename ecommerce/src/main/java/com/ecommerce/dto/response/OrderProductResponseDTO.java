package com.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductResponseDTO {

    private ProductResponseDTO productResponseDTO;
    private int quantity;
    private double subtotal;
}
