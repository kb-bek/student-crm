package com.kaitech.student_crm.services;

import com.kaitech.student_crm.dtos.CreateReportDTO;
import com.kaitech.student_crm.dtos.ReportDTO;
import com.kaitech.student_crm.dtos.ReportResponseDTO;
import com.kaitech.student_crm.exceptions.ActivityNotFoundException;
import com.kaitech.student_crm.exceptions.ReportNotFoundException;
import com.kaitech.student_crm.models.Report;
import com.kaitech.student_crm.repositories.ReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final StudentUserService studentUserService;
    private final ActivityService activityService;
    private final WeeksdayService weeksdayService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReportService(ReportRepository reportRepository, StudentUserService studentUserService, ActivityService activityService, WeeksdayService weeksdayService, ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.studentUserService = studentUserService;
        this.activityService = activityService;
        this.weeksdayService = weeksdayService;
        this.modelMapper = modelMapper;
    }

    public ReportDTO createReport(CreateReportDTO createReportDTO) {
        Report report = new Report();
        report.setUser(studentUserService.getStudentById(createReportDTO.getUserId()));
        report.setActivity(activityService.findById(createReportDTO.getActivityId()).orElseThrow(() -> new ActivityNotFoundException("Activity not found with id " + createReportDTO.getActivityId())));
        report.setWeeksday(weeksdayService.getWeeksdayById(createReportDTO.getWeeksdayId()));
        report.setDone(createReportDTO.isDone());

        Report savedReport = reportRepository.save(report);

        return convertToReportDTO(savedReport);
    }

    public Optional<ReportResponseDTO> getReportById(Long id) {
        return reportRepository.findReportById(id);
    }

    public List<ReportResponseDTO> getAllReports() {
        return reportRepository.findAllReports();
    }

    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id " + id));
        reportRepository.delete(report);
    }

    private ReportDTO convertToReportDTO(Report report) {
        return modelMapper.map(report,  ReportDTO.class);
    }
}
