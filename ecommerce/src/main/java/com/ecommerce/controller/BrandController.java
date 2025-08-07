package com.ecommerce.controller;

import com.ecommerce.dto.request.BrandRequestDTO;
import com.ecommerce.dto.response.BrandResponseDTO;
import com.ecommerce.serviceImpl.BrandServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class BrandController {

    private final BrandServiceImpl brandService;

    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getALlBrands(){
        List<BrandResponseDTO> brandResponseDTOS = brandService.getAllBrands();
        return ResponseEntity.ok(brandResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> getBrandById(@PathVariable(name = "id")Long id){
        BrandResponseDTO brandResponseDTO = brandService.getBrandById(id);
        return ResponseEntity.ok(brandResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BrandResponseDTO> createBrand(@Valid @RequestBody BrandRequestDTO brandRequestDTO){
        BrandResponseDTO brandResponseDTO = brandService.createBrand(brandRequestDTO);
        return new ResponseEntity<>(brandResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BrandResponseDTO> updateBrand(@Valid @RequestBody BrandRequestDTO brandRequestDTO,@PathVariable(name = "id")Long id){
        BrandResponseDTO brandResponseDTO = brandService.updateBrand(brandRequestDTO,id);
        return ResponseEntity.ok(brandResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "id")Long id){
        brandService.deleteBrand(id);
        return  ResponseEntity.noContent().build();
    }
}
