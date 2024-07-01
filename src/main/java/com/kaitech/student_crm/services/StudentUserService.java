package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.UserDTO;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.roles.ERole;
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


    public User createStudent(StudentDataRequest student) {
        User newStudent = new User();

        newStudent.setFirstname(student.getFirstname());
        newStudent.setLastname(student.getLastname());
        newStudent.setEmail(student.getEmail());
        newStudent.setPhoneNumber(student.getPhoneNumber());
        newStudent.getRoles().add(ERole.ROlE_STUDENT);

        try {
            LOGGER.info("Saving Student {}", student.getEmail());
            return studentUserRepository.save(newStudent);
        }catch (Exception e) {
            LOGGER.error("Error during registration, {}", e.getMessage());
            throw new UserExistException("The student " + newStudent.getFirstname() + " " + newStudent.getLastname() + " already exist");
        }
    }

    public User updateStudent(Long studentId, StudentDTO updatedStudent) {
        Optional<User> studentOptional = studentUserRepository.findById(studentId);

        if(studentOptional.isPresent()) {
            User student = studentOptional.get();
            student.setFirstname(updatedStudent.getFirstname());
            student.setLastname(updatedStudent.getLastname());
            student.setEmail(updatedStudent.getEmail());
            student.setPhoneNumber(updatedStudent.getPhoneNumber());
            return studentUserRepository.save(student);
        } else {
            throw new UserExistException("Student not found with id - " + studentId);
        }
    }

    public List<User> getAllStudents() {
        return studentUserRepository.findByRolesContaining(ERole.ROlE_STUDENT);
    }

    public User getStudentById(Long studentId) {
        return studentUserRepository.findUserById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student cannot be found"));
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        User student = getStudentById(studentId);
        studentUserRepository.delete(student);
    }


}
