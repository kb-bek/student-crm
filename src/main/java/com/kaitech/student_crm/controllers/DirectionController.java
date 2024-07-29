package com.kaitech.student_crm.controllers;


import com.kaitech.student_crm.dtos.DirectionDTO;
import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.models.Direction;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.payload.request.DirectionCreateRequest;
import com.kaitech.student_crm.payload.response.DirectionResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.DirectionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directions")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DirectionController {

    @Autowired
    private DirectionService directionService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<DirectionResponse>> getAllDirectionsWithStudents() {
        List<DirectionResponse> directions = directionService.getAllDirections();
        if (directions.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(directions);
    }

    @GetMapping("/{directionId}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public ResponseEntity<DirectionResponse> getDirectorWithStudents(@PathVariable Long directionId) {
        DirectionResponse response = directionService.getById(directionId);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DirectionResponse> createDirection(@RequestBody @Valid DirectionCreateRequest directionCreateRequest) {
        DirectionResponse createdDirection = directionService.createDirection(directionCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDirection);
    }

    @PutMapping("/{directionId}/assignStudent/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> assignStudentToDirection(
            @PathVariable Long directionId,
            @PathVariable Long studentId
    ) {
        Direction direction = directionService.assignStudentToDirection(directionId, studentId);
        return ResponseEntity.ok(new MessageResponse("Assigned successfully"));
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
