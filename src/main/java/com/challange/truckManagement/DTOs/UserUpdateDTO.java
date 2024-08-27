package com.challange.truckManagement.DTOs;

import com.challange.truckManagement.entities.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserUpdateDTO {


    @Email(message = "The email provided is not in the correct format")
    private String email;

    private String name;

    @Size(min = 11, max = 11, message = "CPF must have 11 digits")
    private String cpf;

    private UserRole role;

    private String address;

    private Date birthDate;

}
