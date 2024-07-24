package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.StudentUserService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/student")
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
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return new ResponseEntity<>(studentUserService.getAllStudents(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> addStudent(@Valid @RequestBody StudentDataRequest studentDataRequest,
                                             BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        studentUserService.createStudent(studentDataRequest);
        return ResponseEntity.ok(new MessageResponse("Student added successfully"));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable("id") String studentId) {
        studentUserService.deleteStudent(Long.parseLong(studentId));
        return new ResponseEntity<>(new MessageResponse("Student was deleted"), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Object> updateStudent(@PathVariable("id") String studentId, @Valid @RequestBody StudentDTO studentDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Student student = studentUserService.updateStudent(Long.parseLong(studentId), studentDTO);
        StudentDTO updatedStudent = convertToStudentDTO(student);

        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }


    private StudentDTO convertToStudentDTO(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    private User convertToStudent(StudentDTO studentDTO) {
        return modelMapper.map(studentDTO, User.class);
    }
}
