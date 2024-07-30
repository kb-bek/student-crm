package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.ProjectAlreadyCompletedException;
import com.kaitech.student_crm.exceptions.ProjectNotFoundException;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.request.ProjectRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.repositories.ProjectRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
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
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + projectId + " не найден"));

        LocalDate today = LocalDate.now();
        if (project.getEndDate() != null && !project.getEndDate().isAfter(today)) {
            throw new ProjectAlreadyCompletedException("Нельзя добавлять студентов в завершенный проект");
        }

        List<Student> students = studentUserRepository.findAllById(studentIds);
        project.getStudents().addAll(students);

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

        List<StudentResponse> students = studentUserRepository.findAllByProjectIdResponse(id);
        return new ProjectResponse(
                projectResponse.id(),
                projectResponse.title(),
                projectResponse.description(),
                projectResponse.projectType(),
                students,
                projectResponse.startDate(),
                projectResponse.endDate()
        );
    }

    public ProjectResponse findByIdResponse(Long projectId) {
        ProjectResponse response = projectRepository.findByIdResponse(projectId);
        List<StudentResponse> students = studentUserRepository.findAllByProjectId(projectId);
        return new ProjectResponse(
                response.id(),
                response.title(),
                response.description(),
                response.projectType(),
                students,
                response.startDate(),
                response.endDate()
        );
    }


    public ProjectResponse createProject(ProjectRequest projectRequest, List<Long> studentIds) {
        try {
            if (projectRepository.existsByTitle(projectRequest.getTitle())) {
                throw new IllegalArgumentException("Проект с таким заголовком уже существует");
            }

            LocalDate today = LocalDate.now();

            if (projectRequest.getStartDate() == null || projectRequest.getStartDate().isBefore(today)) {
                throw new IllegalArgumentException("Дата начала должна быть сегодня или в будущем");
            }

            if (projectRequest.getEndDate() != null && !projectRequest.getEndDate().isAfter(projectRequest.getStartDate())) {
                throw new IllegalArgumentException("Дата окончания должна быть не меньше и не равна дате начала");
            }

            Project newProject = new Project();
            newProject.setTitle(projectRequest.getTitle());
            newProject.setDescription(projectRequest.getDescription());
            newProject.setProjectType(projectRequest.getProjectType());
            newProject.setStartDate(projectRequest.getStartDate());
            newProject.setEndDate(projectRequest.getEndDate());
            newProject.getStudents().addAll(studentUserRepository.findAllById(studentIds));

            projectRepository.save(newProject);

            return findByIdResponse(newProject.getId());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла непредвиденная ошибка", e);
        }
    }

    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + id + " не найден"));

        LocalDate oldStartDate = existingProject.getStartDate();
        LocalDate oldEndDate = existingProject.getEndDate();
        LocalDate newStartDate = projectRequest.getStartDate();
        LocalDate newEndDate = projectRequest.getEndDate();

        if (newStartDate != null && newStartDate.isBefore(oldStartDate)) {
            throw new IllegalArgumentException("Новая дата начала не может быть раньше предыдущей даты начала");
        }

        if (newEndDate != null && newEndDate.isBefore(oldEndDate)) {
            throw new IllegalArgumentException("Новая дата окончания не может быть раньше предыдущей даты окончания");
        }

        if (newStartDate != null && newEndDate != null && !newEndDate.isAfter(newStartDate)) {
            throw new IllegalArgumentException("Новая дата окончания должна быть не меньше и не равна новой дате начала");
        }

        if (projectRequest.getTitle() != null && !projectRequest.getTitle().equals(existingProject.getTitle())) {
            boolean titleExists = projectRepository.existsByTitle(projectRequest.getTitle());
            if (titleExists) {
                throw new IllegalArgumentException("Проект с таким названием уже существует");
            }
        }

        existingProject.setTitle(projectRequest.getTitle());
        existingProject.setDescription(projectRequest.getDescription());
        existingProject.setProjectType(projectRequest.getProjectType());
        existingProject.setStartDate(newStartDate != null ? newStartDate : oldStartDate);
        existingProject.setEndDate(newEndDate != null ? newEndDate : oldEndDate);

        projectRepository.save(existingProject);

        return findByIdResponse(existingProject.getId());
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
                () -> new ProjectNotFoundException("Проект с id " + projectId + " не найден"));

        LocalDate today = LocalDate.now();
        if (project.getEndDate() != null && !project.getEndDate().isAfter(today)) {
            throw new ProjectAlreadyCompletedException("Нельзя добавлять студента в завершенный проект");
        }

        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));

        project.getStudents().add(student);
        projectRepository.save(project);

        ProjectResponse projectResponse = projectRepository.findByIdResponse(projectId);
        List<StudentResponse> students = studentUserRepository.findAllByProjectIdResponse(projectId);
        return new ProjectResponse(
                projectResponse.id(),
                projectResponse.title(),
                projectResponse.description(),
                projectResponse.projectType(),
                students,
                projectResponse.startDate(),
                projectResponse.endDate()
        );
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