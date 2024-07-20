package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.ActivityDTO;
import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.WeeksdayDTO;
import com.kaitech.student_crm.models.Activity;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.Weeksday;
import com.kaitech.student_crm.services.ActivityService;
import com.kaitech.student_crm.services.StudentUserService;
import com.kaitech.student_crm.services.WeeksdayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DropdownController {

    private final StudentUserService studentUserService;
    private final ActivityService activityService;
    private final WeeksdayService weeksdayService;
    private final ModelMapper modelMapper;

    @Autowired
    public DropdownController(StudentUserService studentUserService, ActivityService activityService, WeeksdayService weeksdayService, ModelMapper modelMapper) {
        this.studentUserService = studentUserService;
        this.activityService = activityService;
        this.weeksdayService = weeksdayService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {
        return studentUserService.getAllStudents();
    }

    @GetMapping("/activities")
    public List<ActivityDTO> getAllActivities() {
        List<Activity> activities = activityService.findAll();

        return activities.stream()
                .map(this::convertToActivityDTO)
                .collect(Collectors.toList());

    }

    @GetMapping("/weeksdays")
    public List<WeeksdayDTO> getAllWeeksdays() {
        List<Weeksday> weeksdays = weeksdayService.getAllWeeksdays();
        return weeksdays.stream()
                .map(this::convertToWeeksdayDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToStudentDTO(User student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    private ActivityDTO convertToActivityDTO(Activity activity) {
        return modelMapper.map(activity, ActivityDTO.class);
    }

    private WeeksdayDTO convertToWeeksdayDTO(Weeksday weeksday) {
        return modelMapper.map(weeksday, WeeksdayDTO.class);
    }
}
