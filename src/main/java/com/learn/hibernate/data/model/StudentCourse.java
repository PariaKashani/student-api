package com.learn.hibernate.data.model;

import javax.persistence.*;

@Entity(name = "StudentCourse")
@Table(name = "studentCourse")
public class StudentCourse  extends  BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")

    private Course course;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public StudentCourse(){}

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StudentCourse)) return false;

        return id == ((StudentCourse)obj).getId();
    }

    @Override
    public int hashCode() {
        return 29;
    }
}
