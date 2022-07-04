package com.learn.hibernate;

import com.learn.hibernate.data.HibernateUtils;
import com.learn.hibernate.data.model.Admin;
import com.learn.hibernate.data.model.Role;
import com.learn.hibernate.data.model.User;
import com.learn.hibernate.security.PasswordHash;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.Session;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class Application extends ResourceConfig{
    public Application(){

        packages("com.learn.hibernate.controller");

        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Admin admin = new Admin();
        admin.setUserName("imadmin");
        admin.setPassword(PasswordHash.getMd5("1234"));
        admin.setUserRole(Role.ADMIN);
        session.save(admin);
        session.close();
//        packages("com.fasterxml.jackson.jaxrs.json");

        // TODO: 10/8/2018 add an admin
    }
    public static void main(String args[]){

    }
}
