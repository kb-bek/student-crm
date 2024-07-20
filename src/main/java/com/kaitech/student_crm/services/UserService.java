package com.kaitech.student_crm.services;


import com.kaitech.student_crm.dtos.UserDTO;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.roles.ERole;
import com.kaitech.student_crm.payload.request.SignUpRequest;
import com.kaitech.student_crm.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(SignUpRequest user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // изменил роль из админа на роль студента//
        newUser.getRoles().add(ERole.ROlE_STUDENT);

        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username must be unique");
        }
        if(!user.getPassword().equals(user.getConfirmPassword())){
            throw new RuntimeException("Password mismatch");
        }

        try {
            LOGGER.info("Saving User {}", user.getEmail());
            return userRepository.save(newUser);
        }catch (Exception e) {
            LOGGER.error("Error during registration, {}", e.getMessage());
            throw new UserExistException("The user " + newUser.getUsername() + " already exist");
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());

        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
