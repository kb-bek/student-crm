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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "weeksday_id", nullable = false)
    private Weeksday weeksday;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Report() {
    }

    public Report(User user, Activity activity, Weeksday weeksday) {
        this.user = user;
        this.activity = activity;
        this.weeksday = weeksday;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }




    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
