package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentUserRepository extends JpaRepository<Student, Long> {
    Optional<Student> findUserById(Long id);

    @Query("""
            select
            new com.kaitech.student_crm.dtos.StudentDTO(
            u.id,
            u.firstName,
            u.lastName,
            u.email,
            u.phoneNumber
            )
            from Student u
            where u.user.role = :role
            order by u.id
            """)
    List<StudentDTO> findAllStudentDTOByRole(@Param("role") ERole role);

    @Query("""
                        select
                        new com.kaitech.student_crm.dtos.StudentDTO(
                        u.id,
                        u.firstName,
                        u.lastName,
                        u.email,
                        u.phoneNumber
                        )
                        from Student  u
                        where u.user.direction.id = :directorId
                        order by u.id
            """)
    List<StudentDTO> findAllByDirectorId(@Param("directorId") Long directorId);
}