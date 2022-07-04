package com.learn.hibernate.data.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "Admin")
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "admin_id" , referencedColumnName = "id")
public class Admin extends User{
    //todo delete this admin if add to user table is possible
}
