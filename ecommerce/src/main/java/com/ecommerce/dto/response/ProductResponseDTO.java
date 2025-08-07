package com.ecommerce.dto.response;

import com.ecommerce.dto.PhotoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private int stock;
    private double price;

    private CategoryResponseDTO categoryResponseDTO;
    private BrandResponseDTO brandResponseDTO;
    private List<PhotoDTO> photoDTOS;
}
