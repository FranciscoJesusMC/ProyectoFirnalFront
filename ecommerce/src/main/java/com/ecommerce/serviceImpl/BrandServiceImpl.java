package com.ecommerce.serviceImpl;

import com.ecommerce.dto.request.BrandRequestDTO;
import com.ecommerce.dto.response.BrandResponseDTO;
import com.ecommerce.entity.Brand;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.BrandMapper;
import com.ecommerce.repository.BrandRepository;
import com.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper mapper;

    @Override
    public List<BrandResponseDTO> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        if(brands.isEmpty()){
            throw new ResourceNotFoundException("List of brands is empty");
        }
        return brands.stream().map(mapper::toDTO).toList();
    }

    @Override
    public BrandResponseDTO getBrandById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Brand not found with id:" +id));

        return mapper.toDTO(brand);
    }

    @Override
    public BrandResponseDTO createBrand(BrandRequestDTO brandRequestDTO) {
        log.info("Attempt to register brand with name : {}",brandRequestDTO.getName());

        Brand brand = mapper.toEntity(brandRequestDTO);

        Map<String,String> errors = new HashMap<>();

        if(brandRepository.existsByName(brandRequestDTO.getName())){
            errors.put("name","The name :" + brandRequestDTO.getName() + " is already registered");
            log.warn("Attempt to register with duplicate name : {}",brandRequestDTO.getName());
        }

        if (!errors.isEmpty()){
            log.error("Brand creation error {} : {}",brandRequestDTO.getName(),errors);
            throw new EcommerceException(errors);
        }

        Brand newBrand = brandRepository.save(brand);
        log.info("Brand created with name : {}",brandRequestDTO.getName());

        return mapper.toDTO(newBrand);
    }

    @Override
    public BrandResponseDTO updateBrand(BrandRequestDTO brandRequestDTO, Long id) {
        log.info("Attempt to update brand with id: {}",id);
        Brand brand = brandRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Brand not found with id:" +id));

        Map<String,String> errors = new HashMap<>();

        if (brandRequestDTO.getName() !=null && !brandRequestDTO.getName().equals(brand.getName())){
            if (brandRepository.existsByNameAndIdNot(brandRequestDTO.getName(),brand.getId())){
                errors.put("name","The name :" + brandRequestDTO.getName() + "is already registered");
                log.warn("Attempt to update with duplicate name : {}" ,brandRequestDTO.getName());
            }
            brand.setName(brandRequestDTO.getName());
        }

        if(!errors.isEmpty()){
            log.error("Brand update error {} : {}",brandRequestDTO.getName(),errors);
            throw new EcommerceException(errors);
        }

        brandRepository.save(brand);
        log.info("Brand has been updated with id : {}", id);

        return mapper.toDTO(brand);
    }

    @Override
    public void deleteBrand(Long id) {
        log.info("Attempt to delete brand with id: {}",id);
        Brand brand = brandRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Brand not found with id:" +id));

        brandRepository.delete(brand);
        log.info("Brand has been delete with id : {}",id);
    }
}
