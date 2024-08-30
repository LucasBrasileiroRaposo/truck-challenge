package com.challenge.truckManagement.DTOs;

import com.challenge.truckManagement.entities.Client;
import com.challenge.truckManagement.entities.UserRole;
import com.challenge.truckManagement.entities.Vehicle;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
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

    private String id;

    @NotBlank(message = "Email is required")
    @Email(message = "The email provided is not on correct email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 50, message = "Password must have between 5 and 50 characters")
    private String password;

    @NotBlank(message = "Cpf is required")
    @Column(unique = true)
    @Size(message = "Cpf must have 11 digits",min = 11, max = 11)
    @Pattern(
            regexp = "\\d{11}",
            message = "Cpf must have only digits!"
    )
    private String cpf;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must have between 3 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$",
            message = "An name can't have digits!"
    )
    private String name;

    @NotNull(message = "Role is required")
    private UserRole role;

    private List<Vehicle> vehicles;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;

    private String address;


    public UserDTO(Client clientUser) {
        this.id = clientUser.getId();
        this.email = clientUser.getEmail();
        this.password = clientUser.getPassword();
        this.cpf = clientUser.getCpf();
        this.name = clientUser.getName();
        this.role = clientUser.getRole();
        this.address = clientUser.getAddress();
        this.birthDate = clientUser.getBirthDate();
        this.vehicles = clientUser.getVehicles();
    }

    public UserDTO(String id, String email, String cpf, String name, UserRole role) {
        this.id = id;
        this.email = email;
        this.cpf = cpf;
        this.name = name;
        this.role = role;
    }

    public UserDTO(String id, String email, String cpf, String name, UserRole role, String address, Date birthDate, List<Vehicle> vehicles) {
        this.id = id;
        this.email = email;
        this.cpf = cpf;
        this.name = name;
        this.role = role;
        this.address = address;
        this.birthDate = birthDate;
        this.vehicles = vehicles;
    }
}
