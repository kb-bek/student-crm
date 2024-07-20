package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.roles.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentUserRepository extends JpaRepository<User, Long> {

    List<User> findByRolesContaining(ERole role);

    Optional<User> findUserById(Long id);

    boolean existsByUsername(String username);
}
