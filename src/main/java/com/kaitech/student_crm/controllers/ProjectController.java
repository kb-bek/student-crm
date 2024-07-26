package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.ProjectDTO;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.payload.request.ProjectRequest;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Вывод всех проектов")
    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Вывод проекта по id")
    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Создание нового проекта")
    @PostMapping("/create")
    public ProjectResponse createProject(
            @RequestBody ProjectRequest projectRequest,
            @RequestParam(defaultValue = "") List<Long> studIds) {

        return projectService.createProject(projectRequest, studIds);
    }

    @Operation(summary = "Изменение проекта")
    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest projectRequest) {
        return projectService.updateProject(id, projectRequest);
    }

    @Operation(summary = "Удаление проекта")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление студента в проект")
    @PostMapping("/{projectId}/students/{studentId}")
    public ProjectResponse addStudentToProject(@PathVariable Long projectId,
                                               @PathVariable Long studentId) {
        return projectService.addStudentToProject(projectId, studentId);
    }

    @Operation(summary = "Удаление студента из проекта")
    @DeleteMapping("/{projectId}/students/{studentId}")
    public ResponseEntity<HttpStatus> removeStudentFromProject(@PathVariable Long projectId,
                                                    @PathVariable Long studentId) {
        projectService.removeStudentFromProject(projectId, studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
