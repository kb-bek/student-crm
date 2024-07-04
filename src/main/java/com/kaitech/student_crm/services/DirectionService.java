package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.AssignDirectionDTO;
import com.kaitech.student_crm.dtos.DirectionDTO;
import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.exceptions.DirectionNotFoundException;
import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.repositories.DirectionRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirectionService {

    private final DirectionRepository directionRepository;
    private final StudentUserRepository studentUserRepository;

    @Autowired
    public DirectionService(DirectionRepository directionRepository, StudentUserRepository studentUserRepository) {
        this.directionRepository = directionRepository;
        this.studentUserRepository = studentUserRepository;
    }


    public Direction createDirection(String directionName) {
        Direction direction = new Direction();
        direction.setName(directionName);
        return directionRepository.save(direction);
    }

    public Direction assignStudentToDirection(Long directionId, Long studentId) {
        Optional<Direction> optionalDirection = directionRepository.findById(directionId);
        Optional<User> optionalStudent = studentUserRepository.findById(studentId);

        if (optionalDirection.isPresent() && optionalStudent.isPresent()) {
            Direction direction = optionalDirection.get();
            User student = optionalStudent.get();


            student.setDirection(direction);
            studentUserRepository.save(student);

            direction.getStudents().add(student);
            return directionRepository.save(direction);
        } else {
            throw new IllegalArgumentException("Direction or Student not found");
        }
    }

    public Direction getDirectionWithStudents(Long directionId) {
        return directionRepository.findById(directionId).orElse(null);
    }

    public List<Direction> getAllDirectionsWithStudents() {
        return directionRepository.findAll();
    }

    public void deleteDirection(Long id) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new DirectionNotFoundException("Direction not found"));

        directionRepository.delete(direction);
    }

}
