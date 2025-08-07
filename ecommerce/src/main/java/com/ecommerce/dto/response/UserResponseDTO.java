package com.ecommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String address;
    private String dni;
    private String username;
    private String cellphone;


}
