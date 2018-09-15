package com.revenat.serviceLayer.dataAPI_HibernateImpl.sessionFactoryProvider;

import org.hibernate.SessionFactory;

public interface SessionFactoryProvider {
    SessionFactory getSessionFactory();

    void closeSessionFactory(SessionFactory sessionFactory);
}
