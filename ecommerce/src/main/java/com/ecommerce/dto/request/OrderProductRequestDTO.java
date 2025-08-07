package com.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductRequestDTO {

    private Long productId;
    private int quantity;
}
