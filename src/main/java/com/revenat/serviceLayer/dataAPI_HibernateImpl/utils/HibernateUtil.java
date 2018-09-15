package com.revenat.serviceLayer.dataAPI_HibernateImpl.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory() {
        try {
            ServiceRegistry serviceRegistry = configureServiceRegistry();

            sessionFactory = makeSessionFactory(serviceRegistry);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("There was an error building the factory");
        }return sessionFactory;
    }

    private static ServiceRegistry configureServiceRegistry() {
        return new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
    }

    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        return new MetadataSources(serviceRegistry)
                .buildMetadata()
                .buildSessionFactory();
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    public static synchronized void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
