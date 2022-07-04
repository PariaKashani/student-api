package com.learn.hibernate.data.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Master")
@Table(name = "master")
@PrimaryKeyJoinColumn(name = "master_id" , referencedColumnName = "id")
public class Master extends User{
    private String name;

    @ManyToMany(mappedBy = "masterList")
    private Set<Course> courseList;

    public void setCourseList(Set<Course> courseList) {
        this.courseList = courseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourseList() {

        if (courseList ==  null) courseList = new HashSet<>();
        return courseList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Master)) return false;

        return id == ((Master)obj).getId();
    }

    @Override
    public int hashCode() {
        return 23;
    }
}
