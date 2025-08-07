package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private int stock;
    @NotNull
    private double price;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long brandId;
}
