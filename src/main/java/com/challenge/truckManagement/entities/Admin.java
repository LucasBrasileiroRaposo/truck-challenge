package com.challenge.truckManagement.entities;

import com.challenge.truckManagement.DTOs.UserDTO;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Entity(name = "admins")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User{

    public Admin(UserDTO userDTO) {
        super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getCpf(), userDTO.getName(), UserRole.ADMIN);
    }

    public Admin(User user) {
        super(user.getEmail(), user.getPassword(), user.getCpf(), user.getName(), UserRole.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("CLIENT"));

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
