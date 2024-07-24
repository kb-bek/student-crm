package com.kaitech.student_crm.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "levels")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer pointsFrom;
    private Integer pointsTo;
    @Column(length = 900)
    private String description;
    @OneToMany(mappedBy = "level")
    private List<Student> students;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPointsFrom() {
        return pointsFrom;
    }

    public void setPointsFrom(Integer pointsFrom) {
        this.pointsFrom = pointsFrom;
    }

    public Integer getPointsTo() {
        return pointsTo;
    }

    public void setPointsTo(Integer pointsTo) {
        this.pointsTo = pointsTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}