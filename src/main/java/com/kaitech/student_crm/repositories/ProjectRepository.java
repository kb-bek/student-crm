package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.models.Project;
import com.kaitech.student_crm.payload.response.ProjectResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
                select new com.kaitech.student_crm.payload.response.ProjectResponse(
                    p.id,
                    p.title,
                    p.description,
                    p.projectType
                )
                from Project p
            """)
    List<ProjectResponse> getAll();

    @Query("""
            select new com.kaitech.student_crm.payload.response.ProjectResponse(
            p.id,
            p.title,
            p.description,
            p.projectType
            )
            from Project p 
                where p.id = :projectId
            """)
    ProjectResponse findProjectResponseById(@Param("projectId") Long projectId);


    @Query("""
            SELECT new com.kaitech.student_crm.payload.response.ProjectResponse(
                p.id,
                p.title,
                p.description,
                p.projectType
                )
                    from Project p
                    where p.id = :id """)
    ProjectResponse findByIdResponse(@Param(value = "id") Long id);

}