package com.challange.truckManagement.entities;

public enum UserRole {

    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
