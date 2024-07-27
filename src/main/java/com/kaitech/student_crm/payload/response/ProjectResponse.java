package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.models.enums.ProjectType;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private ProjectType projectType;
    private List<StudentResponse> students;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ProjectResponse(){}

    public ProjectResponse(Long id, String title, String description, ProjectType projectType, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectType = projectType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ProjectResponse(Long id, String title, String description, ProjectType projectType, List<StudentResponse> students) {
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

    public List<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponse> students) {
        this.students = students;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
