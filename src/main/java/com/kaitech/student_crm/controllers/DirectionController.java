package com.kaitech.student_crm.controllers;


import com.kaitech.student_crm.dtos.DirectionDTO;
import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.DirectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kaitech.student_crm.payload.request.DirectionCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/directions")
public class DirectionController {

    @Autowired
    private DirectionService directionService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity<List<DirectionResponse>> getAllDirectionsWithStudents() {
        List<Direction> directions = directionService.getAllDirectionsWithStudents();
        if (directions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<DirectionResponse> responseList = directions.stream()
                .map(d -> new DirectionResponse(d.getId(), d.getName(),
                        d.getStudents().stream()
                                .map(this::convertToStudentDTO)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{directionId}")
    public ResponseEntity<DirectionResponse> getDirectionWithStudents(@PathVariable Long directionId) {
        Direction direction = directionService.getDirectionWithStudents(directionId);
        if (direction == null) {
            return ResponseEntity.notFound().build();
        }

        // Преобразование Direction в DirectionResponse
        DirectionResponse response = new DirectionResponse(direction.getId(), direction.getName(),
                direction.getStudents().stream()
                        .map(this::convertToStudentDTO)
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createDirection(@RequestBody DirectionCreateRequest request) {
        Direction direction = directionService.createDirection(request.getName());
        return ResponseEntity.ok(direction);
    }

    @PutMapping("/{directionId}/assignStudent/{studentId}")
    public ResponseEntity<MessageResponse> assignStudentToDirection(
            @PathVariable Long directionId,
            @PathVariable Long studentId
    ) {
        Direction direction = directionService.assignStudentToDirection(directionId, studentId);
        return ResponseEntity.ok(new MessageResponse("Assigned successfully"));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<MessageResponse> deleteDirection(@PathVariable Long id) {
        directionService.deleteDirection(id);
        return ResponseEntity.ok(new MessageResponse("Direction was deleted"));
    }

    private StudentDTO convertToStudentDTO(User student){
        return modelMapper.map(student, StudentDTO.class);
    }

    private DirectionDTO convertToDirectionDTO(Direction direction){
        return modelMapper.map(direction, DirectionDTO.class);
    }
}
