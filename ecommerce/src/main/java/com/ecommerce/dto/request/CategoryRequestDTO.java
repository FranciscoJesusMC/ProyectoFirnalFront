package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDTO {

    @NotBlank
    private String name;
}
