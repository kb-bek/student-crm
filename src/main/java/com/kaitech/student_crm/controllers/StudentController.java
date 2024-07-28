package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.exceptions.EmailAlreadyExistsException;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.Status;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import com.kaitech.student_crm.services.StudentUserService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/student")
@Validated
public class StudentController {

    private final StudentUserService studentUserService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public StudentController(StudentUserService studentUserService, ModelMapper modelMapper, ResponseErrorValidation responseErrorValidation) {
        this.studentUserService = studentUserService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("id") String studentId) {
        Student student = studentUserService.getStudentById(Long.parseLong(studentId));
        StudentDTO studentDTO = convertToStudentDTO(student);
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return new ResponseEntity<>(studentUserService.getAllStudents(), HttpStatus.OK);
    }

    @PostMapping("/add/intern/{directionId}")
    @Operation(method = "Method for adding new interns", description = "Can only be used ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StudentResponse addStudent(@Valid @RequestBody StudentDataRequest studentDataRequest,
                                      @RequestParam Status status,
                                      @PathVariable Long directionId) {
        return studentUserService.createStudent(studentDataRequest, status, directionId);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable("id") String studentId) {
        studentUserService.deleteStudent(Long.parseLong(studentId));
        return new ResponseEntity<>(new MessageResponse("Student was deleted"), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Object> updateStudent(
            @PathVariable("id") String studentId,
            @Valid @RequestBody StudentDTO studentDTO,
            BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        try {
            Student student = studentUserService.updateStudent(Long.parseLong(studentId), studentDTO);
            StudentDTO updatedStudent = convertToStudentDTO(student);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Изменение статуса стажера")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/students/{id}/status")
    public StudentResponse updateStudentStatus(@PathVariable Long id, @RequestParam Status status) {
        return studentUserService.updateStudentStatus(id, status);
    }


    private StudentDTO convertToStudentDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    private User convertToStudent(StudentDTO studentDTO) {
        return modelMapper.map(studentDTO, User.class);
    }



}
