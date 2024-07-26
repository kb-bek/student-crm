package com.kaitech.student_crm.controllers;


import com.kaitech.student_crm.dtos.UserResponse;
import com.kaitech.student_crm.payload.request.LoginRequest;
import com.kaitech.student_crm.payload.request.SignUpRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.security.JWTTokenProvider;
import com.kaitech.student_crm.services.UserService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;



    @PostMapping("/sign/in")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.signIn(loginRequest);
    }

    @PostMapping("/sign/up")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.createUser(signUpRequest);
    }

    @GetMapping("get/code/reset/password/{email}")
    public MessageResponse resetCode(@PathVariable String email) {
        return userService.resetPassword(email);
    }

    @PutMapping("reset/password/{email}/{code}")
    public UserResponse resetPassword(@PathVariable String email,
                                      @PathVariable Integer code,
                                      @RequestParam String password) {
        return userService.newPassword(email, code, password);
    }





}
