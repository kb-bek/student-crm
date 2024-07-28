package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.exceptions.LevelBadRequest;
import com.kaitech.student_crm.exceptions.LevelNotFound;
import com.kaitech.student_crm.payload.request.LevelRequest;
import com.kaitech.student_crm.payload.response.LevelResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/level")
public class LevelController {
    private final LevelService levelService;

    @Autowired
    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("find/by/id/{id}")
    @Operation(summary = "возвращает level по ID")
    public LevelResponse findById(@PathVariable Long id) {
        return levelService.findById(id);
    }

    @GetMapping("find/by/title/{title}")
    @Operation(summary = "возвращает level по title")
    public LevelResponse findByTitle(@PathVariable String title) {
        return levelService.findByTitle(title);
    }

    @GetMapping("find/all")
    @Operation(summary = "возвращает все level, сортируя их по pointFrom по возврастанию")
    public List<LevelResponse> findAll() {
        return levelService.findAll();
    }

    @PostMapping("create/level")
    @Operation(summary = "Сохраняет level, его может использовать только ROLE_ADMIN",
            description = """
                    pointFrom должен быть меньше чем pointTo,
                    pointFrom и pointTo не должны пересекаться с другими баллами уровней,
                    title должен быть уникален.           \s
                    """)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody LevelRequest request) {
        return ResponseEntity.ok(levelService.createLevel(request));
    }

    @ExceptionHandler(LevelBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<MessageResponse> handleLevelBadRequest(LevelBadRequest e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LevelNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<MessageResponse> handleLevelNotFound(LevelNotFound e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}