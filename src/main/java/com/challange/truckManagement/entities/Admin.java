package com.challange.truckManagement.entities;

import com.challange.truckManagement.DTOs.UserDTO;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity(name = "admins")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User{

    public Admin(UserDTO userDTO) {
        super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getCpf(), userDTO.getName(), UserRole.ADMIN);
    }


}
