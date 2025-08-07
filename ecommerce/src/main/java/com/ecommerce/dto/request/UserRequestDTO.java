package com.ecommerce.dto.request;

import com.ecommerce.utils.IUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO implements IUser {

    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String address;
    @NotBlank
    @Size(min = 9,max = 9)
    private String cellphone;
    @NotBlank
    @Size(min = 8,max = 9)
    private String dni;
    @NotBlank
    private String username;
    private String password;

    private boolean admin;
}
