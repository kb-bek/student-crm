package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.enums.ProjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ProjectRequest {


    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private ProjectType projectType;

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

}