package com.challenge.truckManagement.entities;

import com.challenge.truckManagement.DTOs.UserDTO;
import com.challenge.truckManagement.DTOs.VehicleDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
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
    @JsonManagedReference
    private List<Vehicle> vehicles = new ArrayList<>();

    public Client(UserDTO userDTO) {
        super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getCpf(), userDTO.getName(), UserRole.CLIENT);
        this.address = userDTO.getAddress();
        this.birthDate = userDTO.getBirthDate();
    }

    @Transactional
    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.setOwner(this);
    }

    @Transactional
    public void removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.setOwner(null);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("CLIENT"));
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
