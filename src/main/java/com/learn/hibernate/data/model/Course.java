package com.learn.hibernate.data.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Course")
@Table(name = "course")
public class Course extends BaseEntity{

    private String name;

    private int unit;
    @OneToMany(mappedBy = "course" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<StudentCourse> studentCourses;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "course_master",
        joinColumns = @JoinColumn(name = "course_id"),
    inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private Set<Master> masterList = new HashSet<>();


    public Course(){}

    public void addStudentCourse(StudentCourse studentCourse){
        studentCourses.add(studentCourse);
        studentCourse.setCourse(this);
    }

    public void removeStudentCourse(StudentCourse studentCourse){
        studentCourses.remove(studentCourse);
        studentCourse.setStudent(null);
    }
    public Set<Master> getMasterList() {
        if (masterList==null) masterList=new HashSet<>();
        return masterList;
    }

    public void setMasterList(Set<Master> masterList) {
        this.masterList = masterList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public List<StudentCourse> getStudentCourses() {
        if (studentCourses == null) studentCourses = new ArrayList<>();
        return studentCourses;
    }

    public void setStudentCourses(List<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course)) return false;

        return id == ((Course)obj).getId();
    }

    @Override
    public int hashCode() {
        return 31;
    }
}

