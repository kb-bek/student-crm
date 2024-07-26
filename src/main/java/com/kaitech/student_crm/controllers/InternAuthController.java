package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.services.StudentUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/intern/auth")
public class InternAuthController {
    private final StudentUserService studentUserService;

    @Autowired
    public InternAuthController(StudentUserService userService) {
        this.studentUserService = userService;
    }

    @PostMapping("registered/for/student/{email}/{code}")
    @Operation(method = "The student registers using this link")
    public StudentResponse registerStudent(@PathVariable String email,
                                           @PathVariable Integer code,
                                           @RequestParam @Valid @Size(min = 8) String password) {
        return studentUserService.registerStudent(email, code, password);
    }
}