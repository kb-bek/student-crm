package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.annotations.PasswordMatches;
import com.kaitech.student_crm.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class SignUpRequest {
    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8)
    private String password;
    private String confirmPassword;


    public @Email(message = "It should have email format") @NotBlank(message = "User email is required") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "It should have email format") @NotBlank(message = "User email is required") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Please enter your name") String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotEmpty(message = "Please enter your name") String firstname) {
        this.firstname = firstname;
    }

    public @NotEmpty(message = "Please enter your lastname") String getLastname() {
        return lastname;
    }

    public void setLastname(@NotEmpty(message = "Please enter your lastname") String lastname) {
        this.lastname = lastname;
    }

    public @NotEmpty(message = "Please enter your username") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "Please enter your username") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "Password is required") @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "Password is required") @Size(min = 8) String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
