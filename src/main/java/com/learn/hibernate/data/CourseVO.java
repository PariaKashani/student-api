package com.learn.hibernate.data;

import java.util.Date;

public class CourseVO {
    private long id;
    private Date created;
    private Date updated;
    private String name;
    private int unit;

    public CourseVO(){}

    public int getUnit() {
        return unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
