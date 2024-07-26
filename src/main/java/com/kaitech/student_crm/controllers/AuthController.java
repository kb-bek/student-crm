package com.kaitech.student_crm.controllers;


import com.kaitech.student_crm.dtos.UserResponse;
import com.kaitech.student_crm.payload.request.LoginRequest;
import com.kaitech.student_crm.payload.request.SignUpRequest;
import com.kaitech.student_crm.payload.response.JWTTokenSuccessResponse;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.repositories.UserRepository;
import com.kaitech.student_crm.security.JWTTokenProvider;
import com.kaitech.student_crm.security.OAuth2TokenValidator;
import com.kaitech.student_crm.security.SecurityConstants;
import com.kaitech.student_crm.services.OAuth2UserInfoService;
import com.kaitech.student_crm.services.UserService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


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
   @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final OAuth2TokenValidator tokenValidator;
    @Autowired
    private final OAuth2UserInfoService userInfoService;

    public AuthController(RestTemplate restTemplate, OAuth2TokenValidator tokenValidator, OAuth2UserInfoService userInfoService) {
        this.restTemplate = restTemplate;
        this.tokenValidator = tokenValidator;
        this.userInfoService = userInfoService;
    }

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

    @GetMapping("/userInfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam("access_token") String accessToken) {
        Map<String, Object> userInfo = userInfoService.getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/validate-token")
    public String validateToken(@RequestParam("access_token") String accessToken) {
        boolean isValid = tokenValidator.validateToken(accessToken);
        return isValid ? "Token is valid" : "Token is invalid";
    }



}
