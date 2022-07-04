package com.learn.hibernate.data;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {
    public static final SessionFactory SESSION_FACTORY = buildSessionFactory();
    private static SessionFactory buildSessionFactory(){
        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
    }
}
