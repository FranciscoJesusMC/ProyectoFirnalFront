package com.ecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {

    private List<OrderProductRequestDTO> orderProductRequestDTOS;
}
