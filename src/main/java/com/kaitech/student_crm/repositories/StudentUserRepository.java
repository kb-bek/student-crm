package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.enums.ERole;
import com.kaitech.student_crm.payload.response.StudentProjectResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentUserRepository extends JpaRepository<Student, Long> {
    Optional<Student> findUserById(Long id);

    boolean existsByEmail(String email);

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
                        where u.direction.id = :directorId
                        order by u.id
            """)
    List<StudentDTO> findAllByDirectorId(@Param("directorId") Long directorId);

    @Query("""
    select new com.kaitech.student_crm.payload.response.StudentProjectResponse(
    s.id,
    s.image,
    s.firstName,
    s.lastName,
    s.email
    )
    from Student s 
    join s.projects pr 
    where pr.id = :projectId
""")
    List<StudentProjectResponse> findAllByProjectId(@Param(value = "projectId")Long projectId);

    @Modifying
    @Query("DELETE FROM Student sp WHERE sp.projects = :projectId")
    void deleteStudentProjectsByProjectId(@Param("projectId") Long projectId);

}
