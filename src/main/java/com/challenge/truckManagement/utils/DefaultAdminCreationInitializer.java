package com.challenge.truckManagement.utils;

import com.challenge.truckManagement.entities.Admin;
import com.challenge.truckManagement.entities.User;
import com.challenge.truckManagement.entities.UserRole;
import com.challenge.truckManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminCreationInitializer {


    @Autowired
    private UserRepository userRepository;


    @Bean
    public CommandLineRunner createDefaultAdminUser() {
        return args -> {
            String defaultEmail = "admin@example.com";
            if (userRepository.findAll().isEmpty()) {
                User admin = new Admin();
                admin.setEmail(defaultEmail);
                admin.setPassword(new BCryptPasswordEncoder().encode("12345"));
                admin.setRole(UserRole.ADMIN);
                admin.setName("admin");
                admin.setCpf("00000000000");

                userRepository.save(admin);

            }
        };
    }
}
