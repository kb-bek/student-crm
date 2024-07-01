package com.kaitech.student_crm.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ActivityDTO {

    private Long id;

    @NotEmpty(message = "Название не должно быть пустым!")
    @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов!")
    private String title;

    @NotEmpty(message = "Описание не должно быть пустым!")
    @Size(min = 2, max = 100, message = "Описание должно содержать от 2 до 100 символов!")
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
