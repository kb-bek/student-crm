package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.CreateReportDTO;
import com.kaitech.student_crm.dtos.ReportDTO;
import com.kaitech.student_crm.dtos.ReportResponseDTO;
import com.kaitech.student_crm.models.Report;
import com.kaitech.student_crm.services.ActivityService;
import com.kaitech.student_crm.services.ReportService;
import com.kaitech.student_crm.services.StudentUserService;
import com.kaitech.student_crm.services.WeeksdayService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/report")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ReportController {

    private final ReportService reportService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ReportController(ReportService reportService, ResponseErrorValidation responseErrorValidation) {
        this.reportService = reportService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @Operation(summary = "Создать новый отчет")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Object> createReport(@Valid @RequestBody CreateReportDTO createReportDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        ReportDTO reportDTO = reportService.createReport(createReportDTO);
        return new ResponseEntity<>(reportDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Получить отчет по ID")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        Optional<ReportResponseDTO> reportDTO = reportService.getReportById(id);
        return reportDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить все отчеты")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        List<ReportResponseDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }


    @Operation(summary = "Удалить отчет")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "ID отчета", required = true) @PathVariable Long id) {

        reportService.deleteReport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}