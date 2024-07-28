package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.StudentDTOForAll;
import com.kaitech.student_crm.exceptions.EmailAlreadyExistsException;
import com.kaitech.student_crm.exceptions.StudentNotFoundException;
import com.kaitech.student_crm.exceptions.UserExistException;
import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.models.enums.Status;
import com.kaitech.student_crm.payload.request.StudentDataRequest;
import com.kaitech.student_crm.payload.response.StudentResponse;
import com.kaitech.student_crm.repositories.DirectionRepository;
import com.kaitech.student_crm.repositories.StudentUserRepository;
import com.kaitech.student_crm.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StudentUserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final StudentUserRepository studentUserRepository;
    private final DirectionRepository directionRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${link}")
    private String link;

    @Autowired
    public StudentUserService(StudentUserRepository studentUserRepository, DirectionRepository directionRepository, JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentUserRepository = studentUserRepository;
        this.directionRepository = directionRepository;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public StudentResponse createStudent(StudentDataRequest student,
                                         Status status,
                                         Long directionId) {
        if (directionRepository.findById(directionId).isEmpty())
            throw new RuntimeException("There is no such direction.");
        if (studentUserRepository.existsByEmail(student.getEmail()) || userRepository.existsByEmail(student.getEmail()))
            throw new RuntimeException("This email already exists. Email must be unique.");
        Random random = new Random();
        Integer randomCode = random.nextInt(100000000, 999999999);
        Student newStudent = new Student();
        newStudent.setLastName(student.getFirstname());
        newStudent.setFirstName(student.getLastname());
        newStudent.setCode(randomCode);
        newStudent.setImage(student.getImage());
        newStudent.setStatus(status);
        newStudent.setEmail(student.getEmail());
        newStudent.setDirection(directionRepository.findById(directionId).get());
        newStudent.setPhoneNumber(student.getPhoneNumber());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(newStudent.getEmail());
            message.setSubject("Ссылка для регистрации работает только 1 раз");
            message.setText("Ваша ссылка для регистрации:" + link + "/" + newStudent.getEmail() + "/" + randomCode);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Please enter a valid email address.");
        }
        try {
            LOGGER.info("Saving Student {}", student.getEmail());
            studentUserRepository.save(newStudent);
        } catch (Exception e) {
            LOGGER.error("Error during registration, {}", e.getMessage());
            throw new UserExistException("The student " + newStudent.getFirstName() + " " + newStudent.getLastName() + " already exist");
        }
        return findById(newStudent.getId());
    }

    public Student updateStudent(Long studentId, StudentDTO updatedStudentDTO) {
        Optional<Student> studentOptional = studentUserRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            if (!student.getEmail().equals(updatedStudentDTO.getEmail())) {
                boolean emailExists = studentUserRepository.existsByEmail(updatedStudentDTO.getEmail());
                if (emailExists) {
                    throw new EmailAlreadyExistsException("Email " + updatedStudentDTO.getEmail() + " is already in use");
                }
            }

            student.setFirstName(updatedStudentDTO.getFirstname());
            student.setLastName(updatedStudentDTO.getLastname());
            student.setEmail(updatedStudentDTO.getEmail());
            student.setPhoneNumber(updatedStudentDTO.getPhoneNumber());
            return studentUserRepository.save(student);
        } else {
            throw new UserExistException("Student not found with id - " + studentId);
        }
    }


    public List<StudentResponse> getAllStudents() {
        return studentUserRepository.findAllResponse();
    }

    public List<StudentDTOForAll> findAllStudents() {
        return studentUserRepository.findAllStudentDTOs();
    }


    public StudentDTO findStudentById(Long studentId) {
        Student student = studentUserRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Стажер с ID: " + studentId + "не найден"));

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setImage(student.getImage());
        studentDTO.setFirstname(student.getFirstName());
        studentDTO.setLastname(student.getLastName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setStatus(student.getStatus());

        if (student.getDirection() != null) {
            studentDTO.setDirection(student.getDirection().getName());
        }

        List<String> projectNames = student.getProjects().stream()
                .map(Project::getTitle)
                .collect(Collectors.toList());
        studentDTO.setProjects(projectNames);

        return studentDTO;
    }


    public Student getStudentById(Long studentId) {
        return studentUserRepository.findUserById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student cannot be found"));
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = getStudentById(studentId);
        studentUserRepository.delete(student);
    }

    public StudentResponse findById(Long studentId) {
        return studentUserRepository.findByIdResponse(studentId);
    }

    public StudentResponse registerStudent(String email,
                                           Integer code,
                                           String password) {
        Student student = studentUserRepository.findByEmail(email).orElseThrow();
        if (student.isRegistered())
            throw new RuntimeException("This link has already been used");
        if (!Objects.equals(student.getCode(), code) || code.equals(0))
            throw new RuntimeException("This is not a correct link");
        User user = new User(student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                passwordEncoder.encode(password),
                ERole.ROlE_STUDENT, LocalDateTime.now());
        userRepository.save(user);
        student.setCode(0);
        student.setRegistered(true);
        student.setUser(user);
        studentUserRepository.save(student);
        return findById(student.getId());
    }


    @Transactional
    public StudentResponse updateStudentStatus(Long id, Status newStatus) {
        try {
            LOGGER.info("Попытка обновить статус студента с ID: {}", id);

            Student student = getStudentById(id);

            LOGGER.debug("Текущий статус: {}, Новый статус: {}", student.getStatus(), newStatus);

            student.setStatus(newStatus);
            Student updatedStudent = studentUserRepository.save(student);

            LOGGER.info("Статус студента с ID: {} успешно обновлён", id);

            return new StudentResponse(
                    updatedStudent.getId(),
                    updatedStudent.getImage(),
                    updatedStudent.getFirstName(),
                    updatedStudent.getLastName(),
                    updatedStudent.getEmail(),
                    updatedStudent.getPhoneNumber(),
                    updatedStudent.getDirection().getName(), // Assuming Direction is an entity with a getName() method
                    updatedStudent.getStatus()
            );

        } catch (EntityNotFoundException e) {
            LOGGER.error("Ошибка обновления статуса: Студент с ID {} не найден", id, e);
            throw e;

        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка обновления статуса: Недопустимый аргумент для студента с ID: {}", id, e);
            throw e;

        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при обновлении статуса студента с ID: {}", id, e);
            throw new RuntimeException("Не удалось обновить статус студента из-за непредвиденной ошибки.", e);
        }
    }
}