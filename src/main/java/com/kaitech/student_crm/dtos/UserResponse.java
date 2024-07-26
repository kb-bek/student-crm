package com.kaitech.student_crm.dtos;

public class UserResponse {
    Long id;
    String firstName;
    String lastName;
    String email;

    public UserResponse(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}