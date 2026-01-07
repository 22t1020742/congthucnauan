package com.congthucnauan.dto;

import jakarta.validation.constraints.Email;

public class UpdateProfileRequest {
    private String fullName;
    
    @Email(message = "Email should be valid")
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}