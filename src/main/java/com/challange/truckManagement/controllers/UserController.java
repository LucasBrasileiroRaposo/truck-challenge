package com.challange.truckManagement.controllers;

import com.challange.truckManagement.DTOs.AuthenticationDTO;
import com.challange.truckManagement.DTOs.UserDTO;
import com.challange.truckManagement.DTOs.UserUpdateDTO;
import com.challange.truckManagement.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService usersService;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken userData =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(), authenticationDTO.getPassword());

//        Authentication auth = this.authenticationManager.authenticate(userData);

        return new ResponseEntity<>("Login successful!",HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserDTO userDTO) {

        try {

            UserDTO createdUser = this.usersService.createUser(userDTO);
            return new ResponseEntity<>(createdUser,HttpStatus.CREATED);

        } catch (EntityExistsException userAlreadyExists) {
            return new ResponseEntity<>(userAlreadyExists.getMessage(),HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String idStr) {

        try {

            UserDTO userDTOInfo = this.usersService.getUser(idStr);
            return new ResponseEntity<>(userDTOInfo, HttpStatus.OK);

        }catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> filterUsers(@RequestParam(required = false) String cpf,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String role,
                                         @RequestParam(required = false) Date birthDate,
                                         @RequestParam(required = false) String address) {

        try {
            Object queryResult = this.usersService.getUsers(cpf, name, email, role, birthDate, address);
            return new ResponseEntity<>(queryResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String id,@RequestBody @Validated UserUpdateDTO userPatchDTO) {

        try {

            UserDTO updatedUser = this.usersService.updateUserGeneralInfo(id, userPatchDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @PatchMapping("/{userId}/newPassword")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String id,@RequestBody String newPassword) {

        try {

            this.usersService.updateUserPassword(id, newPassword);
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String idStr){
        try {

            this.usersService.deleteUser(idStr);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
