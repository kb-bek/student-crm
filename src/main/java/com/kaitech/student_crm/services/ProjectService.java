package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.ProjectNotFoundException;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.request.ProjectRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.repositories.ProjectRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final StudentUserRepository studentUserRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, StudentUserRepository studentUserRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.studentUserRepository = studentUserRepository;
        this.modelMapper = modelMapper;
    }

    public ProjectResponse saveAllStudentInProject(Long projectId, List<Long> studentIds) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        project.getStudents().addAll(studentUserRepository.findAllById(studentIds));
        projectRepository.save(project);
        return findByIdResponse(project.getId());
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAllResponse();
    }

    public ProjectResponse getProjectById(Long id) {
        ProjectResponse projectResponse = projectRepository.findByIdResponse(id);

        if (projectResponse == null) {
            throw new ProjectNotFoundException("Project with id " + id + " not found");
        }

        projectResponse.setStudents(studentUserRepository.findAllByProjectIdResponse(id));
        return projectResponse;
    }

    public ProjectResponse findByIdResponse(Long projectId) {
        ProjectResponse response = projectRepository.findByIdResponse(projectId);
        response.setStudents(studentUserRepository.findAllByProjectId(projectId) == null ? null : studentUserRepository.findAllByProjectId(projectId));

        return response;
    }

    public ProjectResponse createProject(ProjectRequest projectRequest, List<Long> studentIds) {
        Project newProject = new Project();
        newProject.setTitle(projectRequest.getTitle());
        newProject.setDescription(projectRequest.getDescription());
        newProject.setProjectType(projectRequest.getProjectType());
        newProject.setStartDate(projectRequest.getStartDate());
        newProject.setEndDate(projectRequest.getEndDate());
        newProject.getStudents().addAll(studentUserRepository.findAllById(studentIds));
        projectRepository.save(newProject);
        return findByIdResponse(newProject.getId());
    }


    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
        ProjectResponse projectResponse = getProjectById(id);

        if (projectResponse == null) {
            throw new ProjectNotFoundException("Project with id " + id + " not found");
        }

        projectResponse.setTitle(projectRequest.getTitle());
        projectResponse.setDescription(projectRequest.getDescription());
        projectResponse.setProjectType(projectRequest.getProjectType());
        projectResponse.setStartDate(projectRequest.getStartDate());
        projectResponse.setEndDate(projectRequest.getEndDate());
        projectRepository.save(convertToProject(projectResponse));
        return findByIdResponse(projectResponse.getId());
    }

    public void deleteProject(Long id) {
        ProjectResponse projectResponse = getProjectById(id);

        if (projectResponse == null) {
            throw new ProjectNotFoundException("Project with id " + id + " not found");
        }
        projectRepository.delete(convertToProject(projectResponse));
    }

    public ProjectResponse addStudentToProject(Long projectId, Long studentId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project this id " + projectId + " not found"));
        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        project.getStudents().add(student);
        projectRepository.save(project);
        ProjectResponse projectResponse = projectRepository.findByIdResponse(projectId);
        projectResponse.setStudents(studentUserRepository.findAllByProjectIdResponse(projectId));
        return projectResponse;
    }

    public void removeStudentFromProject(Long projectId, Long studentId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project this id " + projectId + " not found"));
        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        project.getStudents().remove(student);
        projectRepository.save(project);
    }


    public ProjectResponse convertToProjectResponse(Project project) {
        return modelMapper.map(project, ProjectResponse.class);
    }

    public Project convertToProject(ProjectResponse projectResponse) {
        return modelMapper.map(projectResponse, Project.class);
    }


}