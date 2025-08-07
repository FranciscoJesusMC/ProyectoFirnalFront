package com.ecommerce.utils;

import com.ecommerce.entity.Brand;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.BrandRepository;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartingApp implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        Brand brand = new Brand();
        brand.setName("Gloria");
        if (!brandRepository.existsByName(brand.getName())) {
            brandRepository.save(brand);
        }

        Category category = new Category();
        category.setName("Drinks");
        if (!categoryRepository.existsByName(category.getName())) {
            categoryRepository.save(category);
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));


            User admin = new User();
            admin.setName("admin");
            admin.setLastname("admin");
            admin.setEmail("admin@example.com");
            admin.setAddress("admin street");
            admin.setCellphone("ADMINUMBER");
            admin.setDni("ADMINUMBER");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("12345"));
           admin.setRoles(List.of(adminRole));
        if (!userRepository.existsByEmail("admin@example.com")) {
            userRepository.save(admin);
            System.out.println("️Usuario admin creado.");
        }


    }
}
