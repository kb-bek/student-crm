package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentUserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final StudentUserRepository studentUserRepository;

    @Autowired
    public StudentUserService(StudentUserRepository studentUserRepository) {
        this.studentUserRepository = studentUserRepository;
    }


    public Student createStudent(StudentDataRequest student) {
        Student newStudent = new Student();

        newStudent.setLastName(student.getFirstname());
        newStudent.setFirstName(student.getLastname());
        newStudent.setEmail(student.getEmail());
        newStudent.setPhoneNumber(student.getPhoneNumber());

        if(studentUserRepository.existsByUsername(student.getUsername())){
            throw new RuntimeException("Username must be unique");
        }

        if(!student.getConfirmPassword().equals(student.getPassword())){
            throw new RuntimeException("Password mismatch");
        }

        try {
            LOGGER.info("Saving Student {}", student.getEmail());
            return studentUserRepository.save(newStudent);
        } catch (Exception e) {
            LOGGER.error("Error during registration, {}", e.getMessage());
            throw new UserExistException("The student " + newStudent.getFirstName() + " " + newStudent.getLastName() + " already exist");
        }
    }

    public Student updateStudent(Long studentId, StudentDTO updatedStudent) {
        Optional<Student> studentOptional = studentUserRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setFirstName(updatedStudent.getFirstname());
            student.setLastName(updatedStudent.getLastname());
            student.setEmail(updatedStudent.getEmail());
            student.setPhoneNumber(updatedStudent.getPhoneNumber());
            return studentUserRepository.save(student);
        } else {
            throw new UserExistException("Student not found with id - " + studentId);
        }
    }

    public List<StudentDTO> getAllStudents() {
        return studentUserRepository.findAllStudentDTOByRole(ERole.ROlE_STUDENT);
    }

    public Student getStudentById(Long studentId) {
        return studentUserRepository.findUserById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student cannot be found"));
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = getStudentById(studentId);
        studentUserRepository.delete(student);
    }


}
