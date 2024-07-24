package com.kaitech.student_crm.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "weeksday_id", nullable = false)
    private Weeksday weeksday;

    @Column(nullable = false)
    private boolean isDone;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Report() {
    }

    public Report(Activity activity, Weeksday weeksday) {
        this.activity = activity;
        this.weeksday = weeksday;
        this.isDone = isDone();
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    public Student getStudent() {
        return student;
    }
    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Weeksday getWeeksday() {
        return weeksday;
    }

    public void setWeeksday(Weeksday weeksday) {
        this.weeksday = weeksday;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
