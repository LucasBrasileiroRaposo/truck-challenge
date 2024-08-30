package com.challenge.truckManagement.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {

    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    @JsonValue
    public String getRoleName() {
        return roleName;
    }

}
