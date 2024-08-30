package com.challenge.truckManagement.services;

import com.challenge.truckManagement.DTOs.UserDTO;
import com.challenge.truckManagement.DTOs.UserUpdateDTO;
import com.challenge.truckManagement.entities.*;
import com.challenge.truckManagement.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleService vehicleService;

    public User findUserByEmail(String userEmail) {

        Optional<User> userOptional = this.userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException(String.format("No user exists with the email: %s", userEmail));

        return userOptional.get();

    }

    public UserDTO createUser(UserDTO userDTO) {

        String originalPass = userDTO.getPassword();
        userDTO.setPassword(new BCryptPasswordEncoder().encode(originalPass));

        User newUser;
        if (userDTO.getRole().getRoleName().equals("ADMIN")) {
            newUser = new Admin(userDTO);
        } else {
            newUser = new Client(userDTO);
        }

        try{

            User createdUser = this.userRepository.save(newUser);
            if (userDTO.getRole().getRoleName().equals("ADMIN")) {
                return new UserDTO(createdUser.getId(), createdUser.getEmail(), createdUser.getCpf(), createdUser.getName(), createdUser.getRole());
            } else {
                return new UserDTO((Client) createdUser);
            }

        } catch (DataIntegrityViolationException ex) {
            throw new EntityExistsException("Entity with provided CPF or Email already exists.");
        } catch (Exception e) {
            throw new RuntimeException("Problem saving the data occurred, this could be caused by a instability with DB connection");
        }
    }

    public UserDTO getUser(String id) {

        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %s", id));
        }

        User user = userOptional.get();
        if (user.getRole() == UserRole.ADMIN){
            Admin userAdmin = (Admin) user;
            return new UserDTO(user.getId(), userAdmin.getEmail(), userAdmin.getCpf(),userAdmin.getName(), userAdmin.getRole());
        } else {
            Client userClient = (Client) user;
            return new UserDTO(userClient.getId(), userClient.getEmail(), userClient.getCpf(), userClient.getName(), userClient.getRole(), userClient.getAddress(), userClient.getBirthDate(), userClient.getVehicles());
        }


    }

    public List<UserDTO> getUsers(String cpf, String name, String email, String role, Date birthDate, String address) {

        List<User> usersFiltered = this.userRepository.filterUsers(cpf, name, email, role, birthDate, address);

        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : usersFiltered) {
            UserDTO userDTO;
            if (user instanceof Client) {
                userDTO = new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getName(),
                        user.getRole(),
                        ((Client) user).getAddress(),
                        ((Client) user).getBirthDate(),
                        ((Client) user).getVehicles()
                );
            } else {
                userDTO = new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getName(),
                        user.getRole());
            }

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    public UserDTO updateUserGeneralInfo(String id, UserUpdateDTO userPatchDTO) {

        Optional<User> userOptional = this.userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %s", id));
        }

        User user = userOptional.get();

        if (userPatchDTO.getEmail() != null && !userPatchDTO.getEmail().trim().isEmpty()) {
            user.setEmail(userPatchDTO.getEmail());
        }

        if (userPatchDTO.getCpf() != null && !userPatchDTO.getCpf().trim().isEmpty()) {
            user.setCpf(userPatchDTO.getCpf());
        }
        if (userPatchDTO.getName() != null && !userPatchDTO.getName().trim().isEmpty()) {
            user.setName(userPatchDTO.getName());
        }

        if (user.getRole() == UserRole.ADMIN){
            User userAdmin = this.userRepository.save(user);
            return new UserDTO(userAdmin.getId(), userAdmin.getEmail(), userAdmin.getCpf(),userAdmin.getName(), userAdmin.getRole());
        } else {
            Client userClient = (Client) user;
            if (userPatchDTO.getAddress() != null && !userPatchDTO.getAddress().trim().isEmpty()) {
                userClient.setAddress(userPatchDTO.getAddress());
            }
            if (userPatchDTO.getBirthDate() != null) {
                userClient.setBirthDate(userPatchDTO.getBirthDate());
            }
            userClient = this.userRepository.save(userClient);
            return new UserDTO(userClient.getId(), userClient.getEmail(), userClient.getCpf(), userClient.getName(), userClient.getRole(), userClient.getAddress(), userClient.getBirthDate(), userClient.getVehicles());
        }
    }

    public void updateUserPassword(String id, String newPassword) throws BadRequestException {

        if (newPassword.length() < 5) throw new BadRequestException("New password without enough size!");

        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("No user exists with the id: %s", id));
        }

        User user = userOptional.get();
        if (!newPassword.trim().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        } else {
            throw new BadRequestException("Invalid password format!");
        }
        this.userRepository.save(user);

    }


    @Transactional
    public UserDTO addClientVehicle(String id, List<String> vehicleIds) throws BadRequestException {

        if (vehicleIds.isEmpty()) throw new BadRequestException("There is a problem with your vehicle's id list!");

        Client client = checkClient(id);

        List<Vehicle>  vehiclesList = this.vehicleService.checkVehicles(vehicleIds);

        for (Vehicle vehicle: vehiclesList) {
            if(!client.getVehicles().contains(vehicle)) client.addVehicle(vehicle);
        }

        client = this.userRepository.save(client);

        return new UserDTO(client);
    }

    @Transactional
    public UserDTO removeClientVehicle(String id, List<String> vehicleIds) throws BadRequestException {

        if (vehicleIds.isEmpty()) throw new BadRequestException("There is a problem with your vehicle's id list!");

        Client client = checkClient(id);

        List<Vehicle>  vehiclesList = this.vehicleService.checkVehicles(vehicleIds);

        for (Vehicle vehicle: vehiclesList) {
            if(client.getVehicles().contains(vehicle)) client.removeVehicle(vehicle);
        }

        client = this.userRepository.save(client);

        return new UserDTO(client);
    }

    public void deleteUser(String id) {

        Optional<User> userOptional = this.userRepository.findById(id);

        if (userOptional.isEmpty()) throw new EntityNotFoundException(String.format("No user exists with the id: %s", id));

        if (userOptional.get().getRole() == UserRole.CLIENT) {

            Client client = (Client)userOptional.get();

            if (!client.getVehicles().isEmpty()) {
                List<Vehicle> clientVehicles = new ArrayList<>(client.getVehicles());
                for (Vehicle vehicle : clientVehicles) {
                    client.removeVehicle(vehicle);
                }
            }

            this.userRepository.save(client);
        }
        this.userRepository.deleteById(id);
    }

    public Client checkClient(String ownerId) throws BadRequestException {

        Optional<User> optionalClient= this.userRepository.findById(ownerId);
        if (optionalClient.isEmpty()) throw new EntityNotFoundException(String.format("No user exists with the id: %s", ownerId));
        if (optionalClient.get().getRole() != UserRole.CLIENT) throw new BadRequestException("The informed User is not a Client");

        Client potentialClient = (Client) optionalClient.get();

        return potentialClient;
    }
}
