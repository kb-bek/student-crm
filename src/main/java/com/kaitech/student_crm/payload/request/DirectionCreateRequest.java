package com.kaitech.student_crm.payload.request;

import jakarta.validation.constraints.NotEmpty;

public class DirectionCreateRequest {
    @NotEmpty
    private String name;

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
