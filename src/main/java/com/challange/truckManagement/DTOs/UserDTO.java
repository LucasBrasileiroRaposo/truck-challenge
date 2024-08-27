package com.challange.truckManagement.DTOs;

import com.challange.truckManagement.entities.*;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull(message = "Email is required")
    @Email(message = "The email provided is not on correct email format")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 5, max = 50, message = "Password must have between 5 and 50 characters")
    private String password;

    @NotNull(message = "Cpf is required")
    @Column(unique = true)
    @Size(message = "Cpf must have 11 digits",min = 11, max = 11)
    private String cpf;

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must have between 3 and 50 characters")
    private String name;

    @NotNull(message = "Role is required")
    private UserRole role;

    private List<Vehicle> vehicles;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;

    private String address;


    public UserDTO(Admin adminUser) {

        this.email = adminUser.getEmail();
        this.password = adminUser.getPassword();
        this.cpf = adminUser.getCpf();
        this.name = adminUser.getName();
        this.role = adminUser.getRole();

    }

    public UserDTO(Client clientUser) {
        this.email = clientUser.getEmail();
        this.password = clientUser.getPassword();
        this.cpf = clientUser.getCpf();
        this.name = clientUser.getName();
        this.role = clientUser.getRole();
        this.address = clientUser.getAddress();
        this.birthDate = clientUser.getBirthDate();
        this.vehicles = clientUser.getVehicles();
    }

    public UserDTO(String email, String cpf, String name, UserRole role) {
        this.email = email;
        this.cpf = cpf;
        this.name = name;
        this.role = role;
    }

    public UserDTO(String email, String cpf, String name, UserRole role, String address, Date birthDate, List<Vehicle> vehicles) {
        this.email = email;
        this.cpf = cpf;
        this.name = name;
        this.role = role;
        this.address = address;
        this.birthDate = birthDate;
        this.vehicles = vehicles;
    }

}
