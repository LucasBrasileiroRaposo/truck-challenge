package com.challange.truckManagement.services;

import com.challange.truckManagement.DTOs.UserDTO;
import com.challange.truckManagement.DTOs.UserUpdateDTO;
import com.challange.truckManagement.entities.*;
import com.challange.truckManagement.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {

        String originalPass = userDTO.getPassword();
        userDTO.setPassword(new BCryptPasswordEncoder().encode(originalPass));

        User newUser;
        if (userDTO.getRole().equals("ADMIN")) {
            newUser = new Admin(userDTO);
        } else {
            newUser = new Client(userDTO);
        }

        try{

            User createdUser = this.userRepository.save(newUser);
            if (userDTO.getRole().equals("ADMIN")) {
                return new UserDTO((Admin) createdUser);
            } else {
                return new UserDTO((Client) createdUser);
            }

        } catch (Exception e) {
            throw new RuntimeException("Problem saving the data occurred, this could be caused by a instability with DB connection");
        }
    }

    public UserDTO getUser(String id) {

        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %d", id));
        }

        User user = userOptional.get();
        if (user.getRole() == UserRole.ADMIN){
            Admin userAdmin = (Admin) user;
            return new UserDTO(userAdmin.getEmail(), userAdmin.getCpf(),userAdmin.getName(), userAdmin.getRole());
        } else {
            Client userClient = (Client) user;
            return new UserDTO(userClient.getEmail(), userClient.getCpf(), userClient.getName(), userClient.getRole(), userClient.getAddress(), userClient.getBirthDate(), userClient.getVehicles());
        }


    }

    public Object getUsers(String cpf, String name, String email, String role, Date birthDate, String address) {

        return this.userRepository.filterUsers(cpf, name, email, role, birthDate, address);

    }

    public UserDTO updateUserGeneralInfo(String id, UserUpdateDTO userPatchDTO) {

        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %d", id));
        }

        User user = userOptional.get();

        if (userPatchDTO.getEmail() != null) {
            user.setEmail(userPatchDTO.getEmail());
        }

        if (userPatchDTO.getCpf() != null) {
            user.setCpf(userPatchDTO.getCpf());
        }
        if (userPatchDTO.getName() != null) {
            user.setName(userPatchDTO.getName());
        }
        if (userPatchDTO.getRole() != null) {
            user.setRole(userPatchDTO.getRole());
        }


        if (user.getRole() == UserRole.ADMIN){
            Admin userAdmin = (Admin) this.userRepository.save(user);
            return new UserDTO(userAdmin.getEmail(), userAdmin.getCpf(),userAdmin.getName(), userAdmin.getRole());
        } else {
            Client userClient = (Client) user;
            if (userPatchDTO.getAddress() != null) {
                userClient.setAddress(userPatchDTO.getAddress());
            }
            if (userPatchDTO.getBirthDate() != null) {
                userClient.setBirthDate(userPatchDTO.getBirthDate());
            }
            this.userRepository.save(userClient);
            return new UserDTO(userClient.getEmail(), userClient.getCpf(), userClient.getName(), userClient.getRole(), userClient.getAddress(), userClient.getBirthDate(), userClient.getVehicles());
        }
    }

    public void updateUserPassword(String id, String newPassword) {

        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %d", id));
        }

        User user = userOptional.get();
        if (newPassword != null) {
            String originalPass = newPassword;
            user.setPassword(new BCryptPasswordEncoder().encode(originalPass));
        }
        this.userRepository.save(user);

    }


    public void deleteUser(String id) {

        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %d", id));
        }

        this.userRepository.deleteById(id);

    }
}
