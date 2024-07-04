package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.dtos.StudentDTO;

import java.util.List;

public class DirectionResponse {
    private Long id;
    private String name;
    private List<StudentDTO> students;


    public DirectionResponse() {
    }

    public DirectionResponse(Long id, String name, List<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}
