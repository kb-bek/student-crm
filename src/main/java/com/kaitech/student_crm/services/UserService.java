package com.kaitech.student_crm.services;


import com.kaitech.student_crm.dtos.UserDTO;
import com.kaitech.student_crm.dtos.UserResponse;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.payload.request.SignUpRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${linkReset}")
    private String linkForReset;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse newPassword(String email,
                                    Integer code,
                                    String password) {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        if (!Objects.equals(user.getCode(), code) || code.equals(0))
            throw new RuntimeException("This is not a correct link");
        user.setPassword(passwordEncoder.encode(password));
        user.setCode(0);
        userRepository.save(user);
        return userRepository.findByIdResponse(user.getId());
    }

    public MessageResponse resetPassword(String email) {
        if (userRepository.findUserByEmail(email).isEmpty())
            return new MessageResponse("Not found email");
        User user = userRepository.findUserByEmail(email).orElseThrow();
        Random random = new Random();
        Integer randomCode = random.nextInt(100000000, 999999999);
        user.setCode(randomCode);
        userRepository.save(user);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Ссылка для регистрации работает только 1 раз");
            message.setText("Ваша ссылка для регистрации:" + linkForReset + "/" + email + "/" + randomCode);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Please enter a valid email address.");
        }
        return new MessageResponse("The link has been sent to your email.");
    }

    public User createUser(SignUpRequest user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setRole(ERole.ROLE_ADMIN);

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Username must be unique");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new RuntimeException("Password mismatch");
        }

        try {
            LOGGER.info("Saving User {}", user.getEmail());
            return userRepository.save(newUser);
        } catch (Exception e) {
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
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
