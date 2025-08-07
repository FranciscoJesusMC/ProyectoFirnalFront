package com.ecommerce.serviceImpl;

import com.ecommerce.dto.request.UserRequestDTO;
import com.ecommerce.dto.response.UserResponseDTO;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.UserMapper;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import com.ecommerce.utils.IUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User>  users = userRepository.findAll();
        if(users.isEmpty()){
            throw new ResourceNotFoundException("The list of users is empty");
        }
        return users.stream().map(userMapper::toDTO).toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id : " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        log.info("Starting user creation with username: {}", userRequestDTO.getUsername());

        User user = userMapper.toEntity(userRequestDTO);

        Map<String,String> errors = new HashMap<>();

        if(userRepository.existsByUsername(userRequestDTO.getUsername())){
            errors.put("username","The username :"+ userRequestDTO.getUsername()+" is already registered");
            log.warn("Attempt to register with duplicate username: {}" ,userRequestDTO.getUsername());
        }
        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            errors.put("email","The email :" + userRequestDTO.getEmail()+" is already registered");
            log.warn("Attempt to register with duplicate email: {}", userRequestDTO.getEmail());
        }
        if (userRepository.existsByCellphone(userRequestDTO.getCellphone())){
            errors.put("cellphone","The cellphone :" + userRequestDTO.getCellphone()+" is already registered");
            log.warn("Attempt to register with duplicate cellphone: {}",userRequestDTO.getCellphone());
        }
        if (userRepository.existsByDni(userRequestDTO.getDni())){
            errors.put("dni","The dni :"+ userRequestDTO.getDni()+" is already registered");
            log.warn("Attempt to register with duplicate dni: {}",userRequestDTO.getDni());
        }

        if (!errors.isEmpty()){
            log.error("User creation error {}: {}", userRequestDTO.getUsername(), errors);
            throw new EcommerceException(errors);
        }

        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User newUser = userRepository.save(user);
        log.info("User has been created: {}",userRequestDTO.getUsername());

        return userMapper.toDTO(newUser);
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO, Long id) {
        log.info("Starting user update with username: {}",userRequestDTO.getUsername());

        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id : " + id));

        Map<String,String> errors = new HashMap<>();

        if(userRequestDTO.getUsername() !=null && !userRequestDTO.getUsername().equals(user.getUsername())){
            if(userRepository.existsByUsernameAndIdNot(userRequestDTO.getUsername(),user.getId())){
                errors.put("username","The username :"+ userRequestDTO.getUsername()+ " is already registered");
                log.warn("Attempt to register with duplicate username: {}",userRequestDTO.getUsername());
            }
            user.setUsername(userRequestDTO.getUsername());
        }
        if(userRequestDTO.getEmail() !=null && !userRequestDTO.getEmail().equals(user.getEmail())){
            if(userRepository.existsByEmailAndIdNot(userRequestDTO.getEmail(),user.getId())){
                errors.put("email","The email :" + userRequestDTO.getEmail()+" is already registered");
                log.warn("Attempt to register with duplicate email: {}", userRequestDTO.getEmail());
            }
            user.setEmail(userRequestDTO.getEmail());
        }
        if(userRequestDTO.getCellphone() !=null && !userRequestDTO.getCellphone().equals(user.getCellphone())){
            if(userRepository.existsByCellphoneAndIdNot(userRequestDTO.getCellphone(), user.getId())){
                errors.put("cellphone","The cellphone :" + userRequestDTO.getCellphone()+ " is already registered");
                log.warn("Attempt to register with duplicate cellphone: {}",userRequestDTO.getCellphone());
            }
            user.setCellphone(userRequestDTO.getCellphone());
        }
        if (userRequestDTO.getDni() !=null && !userRequestDTO.getDni().equals(user.getDni())){
            if (userRepository.existsByDniAndIdNot(userRequestDTO.getDni(),user.getId())){
                errors.put("dni","The dni :" + userRequestDTO.getDni()+" is already registered");
                log.warn("Attempt to register with duplicate dni: {}",userRequestDTO.getDni());
            }
            user.setDni(userRequestDTO.getDni());
        }

        if (!errors.isEmpty()){
            log.error("User creation error {}:{}",userRequestDTO.getUsername(),errors);
            throw new EcommerceException(errors);
        }

        user.setRoles(getRoles(userRequestDTO));
        user.setName(userRequestDTO.getName());
        user.setLastname(userRequestDTO.getLastname());
        user.setAddress(userRequestDTO.getAddress());
        userRepository.save(user);
        log.info("User has been updated: {}",userRequestDTO.getUsername());


        return userMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Attempt to delete user with id : {}",id);
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id : " + id));
        userRepository.delete(user);
        log.info("User has been delete with id : {}",id);
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        optionalRole.ifPresent(roles::add);

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
}
