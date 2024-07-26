package com.kaitech.student_crm.payload.request;


import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public @NotEmpty(message = "Username cannot be empty") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Username cannot be empty") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Password cannot be empty") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "Password cannot be empty") String password) {
        this.password = password;
    }
}
