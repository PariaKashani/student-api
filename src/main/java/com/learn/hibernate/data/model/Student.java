package com.learn.hibernate.data.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Student")
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "master_id" , referencedColumnName = "id")
public class Student extends User{

    private int tt;
    private String firstName;
    private String lastName;

    private String code;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "student_id")
    private List<Phone> phoneList;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StudentCourse> studentCourses;
    public Student(){}

    public void addStudentCourse(StudentCourse studentCourse){
        studentCourses.add(studentCourse);
        studentCourse.setStudent(this);
    }

    public void removeStudentCourse(StudentCourse studentCourse){
        studentCourses.remove(studentCourse);
        studentCourse.setStudent(null);
    }
    public List<Phone> getPhoneList() {
        if (phoneList == null) phoneList = new ArrayList<>();
        return phoneList;
    }

    public List<StudentCourse> getStudentCourses() {
        if (studentCourses==null) studentCourses=new ArrayList<>();
        return studentCourses;
    }

    public void setStudentCourses(List<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
    }
    public void addPhone(Phone phone){
        this.phoneList.add(phone);
    }
    public void removePhone(Phone phone){
        this.phoneList.remove(phone);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public class Builder{
        Student student;
        public Builder(String firstName , String lastName){

        }
    }

    @Override
    public int hashCode() {
        return 41;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;

        return id == ((Student)obj).getId();
    }
}
