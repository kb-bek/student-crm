package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.ActivityDTO;
import com.kaitech.student_crm.exceptions.ActivityErrorResponse;
import com.kaitech.student_crm.exceptions.ActivityNotCreatedException;
import com.kaitech.student_crm.exceptions.ActivityNotFoundException;
import com.kaitech.student_crm.exceptions.ActivityNotUpdatedException;
import com.kaitech.student_crm.models.Activity;
import com.kaitech.student_crm.services.ActivityService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private  ModelMapper modelMapper;


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public List<Activity> getAllActivity(){
        return activityService.findAll();
    }

    // Метод для вывода Activity по id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    public Optional<Activity> getTaskById(@PathVariable Long id) {
        return activityService.findById(id);
    }

    // Метод для создания Activity
    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> createActivity(@RequestBody @Valid ActivityDTO activityDTO,
                                                     BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(",");
            }

            throw new ActivityNotCreatedException(errorMessage.toString());
        }

        activityService.addActivity(convertToActivity(activityDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    // Метод для обновления Activity
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> updateActivity(@PathVariable Long id, @RequestBody @Valid ActivityDTO activityDTO,
                                                     BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(",");
            }

            throw new ActivityNotUpdatedException(errorMessage.toString());
        }

        activityService.updateActivity(id, convertToActivity(activityDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // Метод для удаления Activity
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ActivityErrorResponse> handleException(ActivityNotFoundException e){
        ActivityErrorResponse activityErrorResponse = new ActivityErrorResponse(
                "Activity with this id wasn't found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(activityErrorResponse, HttpStatus.NOT_FOUND);
    }





    private Activity convertToActivity(ActivityDTO activityDTO) {
        return modelMapper.map(activityDTO, Activity.class);
    }

    private ActivityDTO convertToActivityDTO(Activity activity){
        return modelMapper.map(activity, ActivityDTO.class);
    }


}
