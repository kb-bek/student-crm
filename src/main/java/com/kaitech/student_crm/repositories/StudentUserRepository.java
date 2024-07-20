package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.roles.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentUserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);

    boolean existsByUsername(String username);

    @Query("""
            select
            new com.kaitech.student_crm.dtos.StudentDTO(
            u.id,
            u.firstname,
            u.lastname,
            u.email,
            u.phoneNumber
            )
            from User u
            where :role member of u.roles
            order by u.id
            """)
    List<StudentDTO> findAllStudentDTOByRole(@Param("role") ERole role);

    @Query("""
                        select
                        new com.kaitech.student_crm.dtos.StudentDTO(
                        u.id,
                        u.firstname,
                        u.lastname,
                        u.email,
                        u.phoneNumber
                        )
                        from User  u
                        where u.direction.id = :directorId
                        order by u.id
            """)
    List<StudentDTO> findAllByDirectorId(@Param("directorId") Long directorId);
}
