package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.Activity;
import com.kaitech.student_crm.models.User;
import com.kaitech.student_crm.models.Weeksday;


public class ReportDTO {

    private Long id;
    private Long userId;
    private Long activityId;
    private Long weeksdayId;
    private boolean isDone;



    public ReportDTO(Long id, Long userId, Long activityId, Long weeksdayId, boolean isDone) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.weeksdayId = weeksdayId;
        this.isDone = isDone;
    }

    // Пустой конструктор
    public ReportDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getWeeksdayId() {
        return weeksdayId;
    }

    public void setWeeksdayId(Long weeksdayId) {
        this.weeksdayId = weeksdayId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
