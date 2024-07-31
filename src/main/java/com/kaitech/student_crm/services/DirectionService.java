package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.NotFoundException;
import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.payload.request.DirectionCreateRequest;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import com.kaitech.student_crm.repositories.DirectionRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DirectionService {

    private final DirectionRepository directionRepository;
    private final StudentUserRepository studentUserRepository;

    @Autowired
    public DirectionService(DirectionRepository directionRepository, StudentUserRepository studentUserRepository) {
        this.directionRepository = directionRepository;
        this.studentUserRepository = studentUserRepository;
    }


    public DirectionResponse createDirection(DirectionCreateRequest directionCreateRequest) {
        try {
            if (directionCreateRequest.getName() == null || directionCreateRequest.getName().isEmpty()) {
                throw new IllegalArgumentException("Поле имя не должно быть пустым");
            }
            if (directionCreateRequest.getDescription() == null || directionCreateRequest.getDescription().isEmpty()) {
                throw new IllegalArgumentException("Описание не должно быть пустым");
            }

            if (directionRepository.existsByName(directionCreateRequest.getName())) {
                throw new IllegalArgumentException("Направление с таким именем уже существует");
            }

            Direction direction = new Direction();
            direction.setName(directionCreateRequest.getName());
            direction.setDescription(directionCreateRequest.getDescription());

            Direction savedDirection = directionRepository.save(direction);

            DirectionResponse responseDTO = new DirectionResponse();
            responseDTO.setId(savedDirection.getId());
            responseDTO.setName(savedDirection.getName());
            responseDTO.setDescription(savedDirection.getDescription());

            return responseDTO;

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла ошибка базы данных", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла неожиданная ошибка", e);
        }
    }



    public Direction assignStudentToDirection(Long directionId, Long studentId) {
        Optional<Direction> optionalDirection = directionRepository.findById(directionId);
        Optional<Student> optionalStudent = studentUserRepository.findById(studentId);

        if (optionalDirection.isPresent() && optionalStudent.isPresent()) {
            Direction direction = optionalDirection.get();
            Student student = optionalStudent.get();


            student.setDirection(direction);
            studentUserRepository.save(student);

            direction.getStudents().add(student);
            return directionRepository.save(direction);
        } else {
            throw new NotFoundException("Direction or Student not found");
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
                .orElseThrow(() -> new NotFoundException("Direction not found"));

        directionRepository.delete(direction);
    }

    public List<DirectionResponse> getAllDirections() {
        return directionRepository.findAllDirections();
    }


    public DirectionResponse getById(Long id) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Direction not found with id: " + id));

        DirectionResponse response = new DirectionResponse();
        response.setId(direction.getId());
        response.setName(direction.getName());
        response.setDescription(direction.getDescription());

        return response;
    }
}
