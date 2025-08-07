package com.ecommerce.serviceImpl;

import com.ecommerce.dto.request.CategoryRequestDTO;
import com.ecommerce.dto.response.CategoryResponseDTO;
import com.ecommerce.entity.Category;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;


    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()){
            throw new ResourceNotFoundException("List of categories is empty");
        }
        return categories.stream().map(mapper::toDTO).toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found with id :" + id));

        return mapper.toDTO(category);
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        log.info("Attempt to register category with name : {}",categoryRequestDTO.getName());

        Category category =  mapper.toEntity(categoryRequestDTO);

        Map<String,String> errors = new HashMap<>();

        if(categoryRepository.existsByName(categoryRequestDTO.getName())){
            errors.put("name","The name : " +categoryRequestDTO.getName()+" is already registered");
            log.warn("Attempt to register with duplicate name : {}",categoryRequestDTO.getName());
        }

        if (!errors.isEmpty()){
            log.error("Category error creation {}: {}",categoryRequestDTO.getName(),errors);
            throw new EcommerceException(errors);
        }

        Category newCategory = categoryRepository.save(category);
        log.info("Category has been created :{}",categoryRequestDTO.getName());

        return mapper.toDTO(newCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long id) {
        log.info("Attempt to update category with id : {}" , id);
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found with id :" + id));

        Map<String,String> errors = new HashMap<>();

        if (categoryRequestDTO.getName() !=null && !categoryRequestDTO.getName().equals(category.getName())){
            if (categoryRepository.existsByNameAndIdNot(categoryRequestDTO.getName(),category.getId())){
                errors.put("name","The name : " + categoryRequestDTO.getName() + " is already registered");
                log.warn("Attempt to update with duplicate name : {}", categoryRequestDTO.getName());
            }
            category.setName(categoryRequestDTO.getName());
        }

        if (!errors.isEmpty()){
            log.error("Category update error {} : {}",categoryRequestDTO.getName(),errors);
            throw new EcommerceException(errors);
        }

        categoryRepository.save(category);
        log.info("Category has been updated with id : {}",id );


        return mapper.toDTO(category);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Attempt to delete category with id : {}" , id);
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found with id :" + id));

        categoryRepository.delete(category);
        log.info("Category has been delete with id : {}",id );
    }
}
