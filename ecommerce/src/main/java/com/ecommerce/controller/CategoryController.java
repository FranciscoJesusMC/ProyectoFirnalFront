package com.ecommerce.controller;

import com.ecommerce.dto.request.CategoryRequestDTO;
import com.ecommerce.dto.response.CategoryResponseDTO;
import com.ecommerce.serviceImpl.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getALlCategories(){
        List<CategoryResponseDTO> categoryResponseDTOS = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable(name = "id")Long id){
        CategoryResponseDTO categoryResponseDTOS = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponseDTOS);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO,@PathVariable(name = "id")Long id){
        CategoryResponseDTO categoryResponseDTO = categoryService.updateCategory(categoryRequestDTO,id);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "id")Long id){
        categoryService.deleteCategory(id);
        return  ResponseEntity.noContent().build();
    }
}
