package com.challenge.truckManagement.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationDTO {

    private String login;

    private String password;

}
