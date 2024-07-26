package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.models.enums.ProjectType;

import java.util.List;

public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private ProjectType projectType;
    private List<StudentProjectResponse> students;

    public ProjectResponse(){}

    public ProjectResponse(Long id, String title, String description, ProjectType projectType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectType = projectType;
    }

    public ProjectResponse(Long id, String title, String description, ProjectType projectType, List<StudentProjectResponse> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectType = projectType;
        this.students = students;
    }

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

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public List<StudentProjectResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentProjectResponse> students) {
        this.students = students;
    }
}
