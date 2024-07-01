package com.kaitech.student_crm.services;

import com.kaitech.student_crm.exceptions.ActivityNotFoundException;
import com.kaitech.student_crm.models.Activity;
import com.kaitech.student_crm.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {


    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    // Меетод для вывода всех Activity
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    // Метод для вывода Activity по id
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    // Метод для добавления Activity
    @Transactional
    public Activity addActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    // Метод для обновления Activity
    public Activity updateActivity(Long id, Activity updatedActivity) {
        Optional<Activity> existingActivityOpt = activityRepository.findById(id);
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = existingActivityOpt.get();
            existingActivity.setTitle(updatedActivity.getTitle());
            existingActivity.setDescription(updatedActivity.getDescription());
            return activityRepository.save(existingActivity);
        } else {
            throw new ActivityNotFoundException("Activity not found with id " + id);
        }
    }

    // Метод для удаления Activity по ID
    public void deleteActivity(Long id) {
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
        } else {
            throw new ActivityNotFoundException("Activity not found with id " + id);
        }
    }

}
