package com.challange.truckManagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "users")
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class User {


    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private String id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String cpf;

    private String name;

    private UserRole role;


    public User(String email,String password,String cpf,String name, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.name = name;
        this.role = userRole;
    }
}
