package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.models.enums.Status;

public record StudentResponse(
        Long id,
        String image,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String direction,
        Status status) {
    public StudentResponse(Long id, String image, String firstName, String lastName, String email) {
        this(id, image, firstName, lastName, email, null, null, null);
    }

}
