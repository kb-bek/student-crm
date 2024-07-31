package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.ProjectDTO;
import com.kaitech.student_crm.exceptions.ProjectAlreadyCompletedException;
import com.kaitech.student_crm.exceptions.ProjectNotFoundException;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.payload.request.ProjectRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/project")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Вывод всех проектов")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Вывод проекта по id")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Создание нового проекта")
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProjectResponse createProject(
            @RequestBody ProjectRequest projectRequest,
            @RequestParam(defaultValue = "") List<Long> studIds) {

        return projectService.createProject(projectRequest, studIds);
    }

    @Operation(summary = "Изменение проекта")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest projectRequest) {
        return projectService.updateProject(id, projectRequest);
    }

    @Operation(summary = "Удаление проекта")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление студента в проект")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("add/student/{projectId}/{studentId}")
    public ProjectResponse addStudentToProject(@PathVariable Long projectId,
                                               @PathVariable Long studentId) {
        return projectService.addStudentToProject(projectId, studentId);
    }

    @Operation(summary = "Удаление студента из проекта")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{projectId}/students/{studentId}")
    public ResponseEntity<HttpStatus> removeStudentFromProject(@PathVariable Long projectId,
                                                               @PathVariable Long studentId) {
        projectService.removeStudentFromProject(projectId, studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Добавляет много студентов")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("add/students/{projectId}")
    public ProjectResponse saveAllStudent(@PathVariable Long projectId,
                                          @RequestParam List<Long> studentIds) {
        return projectService.saveAllStudentInProject(projectId, studentIds);
    }
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<MessageResponse> handleProjectNotFoundException(ProjectNotFoundException ex) {
        MessageResponse messageResponse = new MessageResponse("Project not found this id");
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception ex) {
        MessageResponse messageResponse = new MessageResponse("Internal Server Error");
        return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<MessageResponse> handleProjectNotFoundException(StudentNotFoundException ex) {
        MessageResponse messageResponse = new MessageResponse("Student not found this id");
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }
    @ExceptionHandler(ProjectAlreadyCompletedException.class)
    public ResponseEntity<String> handleProjectAlreadyCompletedException(ProjectAlreadyCompletedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}