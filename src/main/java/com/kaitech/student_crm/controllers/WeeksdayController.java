package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.StudentDTO;
import com.kaitech.student_crm.dtos.WeeksdayDTO;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.Weeksday;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.WeeksdayService;
import com.kaitech.student_crm.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/weeksday")
public class WeeksdayController {

    private final WeeksdayService weeksdayService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public WeeksdayController(WeeksdayService weeksdayService, ModelMapper modelMapper, ResponseErrorValidation responseErrorValidation) {
        this.weeksdayService = weeksdayService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weeksday> getWeeksday(@PathVariable("id") String weeksdayId) {
        Weeksday weeksday = weeksdayService.getWeeksdayById(Long.parseLong(weeksdayId));

        return new ResponseEntity<>(weeksday, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Weeksday>> getAllWeeksday(){
        List<Weeksday> weeksdays = weeksdayService.getAllWeeksdays();
        return new ResponseEntity<>(weeksdays, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createWeeksday(@Valid @RequestBody WeeksdayDTO weeksdayDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Weeksday newWeeksday = weeksdayService.createWeeksday(weeksdayDTO);

        return new ResponseEntity<>(newWeeksday, HttpStatus.OK);

    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteWeeksday(@PathVariable("id") String weeksdayId) {
        weeksdayService.deleteWeeksday(Long.parseLong(weeksdayId));
        return new ResponseEntity<>(new MessageResponse("Week's day was deleted"), HttpStatus.OK);
    }

    private WeeksdayDTO convertToStudentDTO(Weeksday weeksday){
        return modelMapper.map(weeksday, WeeksdayDTO.class);
    }

    private Weeksday convertToStudent(WeeksdayDTO weeksdayDTO) {
        return modelMapper.map(weeksdayDTO, Weeksday.class);
    }
}
