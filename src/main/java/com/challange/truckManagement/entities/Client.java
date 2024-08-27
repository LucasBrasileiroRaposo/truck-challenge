package com.challange.truckManagement.entities;

import com.challange.truckManagement.DTOs.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client extends User {

    private Date birthDate;

    private String address;

    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles;

    public Client(UserDTO userDTO) {
        super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getCpf(), userDTO.getName(), UserRole.CLIENT);
        this.address = userDTO.getAddress();
        this.birthDate = userDTO.getBirthDate();
        this.vehicles = userDTO.getVehicles();
    }


}
