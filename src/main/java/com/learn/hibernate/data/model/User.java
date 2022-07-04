package com.learn.hibernate.data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "User")
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity{
    //how can i add multiple roles to a user?=> @ElementCollection
    private String userRole ;
    @Column(unique = true)
    private String userName;
    private String password;


    public User(){}

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRoles) {
        this.userRole = userRoles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
