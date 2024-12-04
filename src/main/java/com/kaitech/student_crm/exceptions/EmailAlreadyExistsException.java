package com.kaitech.student_crm.exceptions;

import jakarta.validation.constraints.NotEmpty;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
