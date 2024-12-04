package com.kaitech.student_crm.repositories;

import com.kaitech.student_crm.dtos.ReportDTO;
import com.kaitech.student_crm.dtos.ReportResponseDTO;
import com.kaitech.student_crm.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT new com.kaitech.student_crm.dtos.ReportResponseDTO(r.id, new com.kaitech.student_crm.dtos.StudentDTO(s.id, s.firstName, s.lastName, s.email, s.phoneNumber), new com.kaitech.student_crm.dtos.ActivityDTO(a.id, a.title, a.description), new com.kaitech.student_crm.dtos.WeeksdayDTO(w.id, w.name), r.isDone) " +
            "FROM Report r JOIN r.student s JOIN r.activity a JOIN r.weeksday w " +
            "WHERE r.id = :id")
    Optional<ReportResponseDTO> findReportById(@Param("id") Long id);

    @Query("SELECT new com.kaitech.student_crm.dtos.ReportResponseDTO(r.id, new com.kaitech.student_crm.dtos.StudentDTO(s.id, s.firstName, s.lastName, s.email, s.phoneNumber), new com.kaitech.student_crm.dtos.ActivityDTO(a.id, a.title, a.description), new com.kaitech.student_crm.dtos.WeeksdayDTO(w.id, w.name), r.isDone) " +
            "FROM Report r JOIN r.student s JOIN r.activity a JOIN r.weeksday w")
    List<ReportResponseDTO> findAllReports();
}
