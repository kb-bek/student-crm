package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.User;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DirectionDTO {
    private Long id;
    @NotEmpty
    private String name;

    private List<StudentDTO> students;


    public DirectionDTO(Long id, String name, List<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public DirectionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty String getName() {
        return name;
    }

    public void setName(@NotEmpty String name) {
        this.name = name;
    }


}
