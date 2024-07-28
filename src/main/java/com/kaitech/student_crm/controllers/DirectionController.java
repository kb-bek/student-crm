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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directions")
public class DirectionController {

    @Autowired
    private DirectionService directionService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity<List<DirectionResponse>> getAllDirectionsWithStudents() {
        List<DirectionResponse> directions = directionService.getAllDirections();
        if (directions.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(directions);
    }

    @GetMapping("/{directionId}")
    public ResponseEntity<DirectionResponse> getDirectorWithStudents(@PathVariable Long directionId) {
        DirectionResponse response = directionService.getById(directionId);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<DirectionDTO> createDirection(@RequestBody DirectionDTO directionDTO) {
        DirectionDTO createdDirection = directionService.createDirection(directionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDirection);
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

    private StudentDTO convertToStudentDTO(User student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    private DirectionDTO convertToDirectionDTO(Direction direction) {
        return modelMapper.map(direction, DirectionDTO.class);
    }
}
