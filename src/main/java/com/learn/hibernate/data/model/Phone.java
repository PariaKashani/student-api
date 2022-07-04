package com.learn.hibernate.data.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Phone")
@Table(name = "phone")
public class Phone extends BaseEntity {
    private String number;

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Phone)) return false;

        return id == ((Phone)obj).getId();
    }

    @Override
    public int hashCode() {
        return 37;
    }
}
