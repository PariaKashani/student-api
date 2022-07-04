package com.learn.hibernate.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learn.hibernate.data.model.Phone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentVO {
    //value object
    private long id;
    private Date created;
    private Date updated;
//    @JsonProperty("first-name")
    private String firstName;
//    @JsonProperty("last-name")
    private String lastName;
    private String code;
    private List<String> phones;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    //setter abg getters


    public List<String> getPhones() {
        if (phones==null) phones=new ArrayList<>();
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
