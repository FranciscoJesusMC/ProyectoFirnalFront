package com.ecommerce.repository;

import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByCellphone(String username);
    boolean existsByDni(String dni);

    boolean existsByEmailAndIdNot(String email,Long id);
    boolean existsByUsernameAndIdNot(String username,Long id);
    boolean existsByCellphoneAndIdNot(String cellphone,Long id);
    boolean existsByDniAndIdNot(String dni,Long id);

    Optional<User> findByUsername(String username);
}
