package com.kaitech.student_crm.dtos;
public class UserResponse {
    Long id;
    String username;
    String firstName;
    String lastName;
    String email;

    public UserResponse(Long id, String username, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}