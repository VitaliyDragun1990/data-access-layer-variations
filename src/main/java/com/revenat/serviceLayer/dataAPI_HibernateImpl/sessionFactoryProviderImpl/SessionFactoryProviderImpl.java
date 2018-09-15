package com.revenat.serviceLayer.dataAPI_HibernateImpl.sessionFactoryProviderImpl;

import com.revenat.serviceLayer.dataAPI_HibernateImpl.sessionFactoryProvider.SessionFactoryProvider;
import com.revenat.serviceLayer.dataAPI_HibernateImpl.utils.HibernateUtil;
import org.hibernate.SessionFactory;

public class SessionFactoryProviderImpl implements SessionFactoryProvider {
    @Override
    public SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    @Override
    public void closeSessionFactory(SessionFactory sessionFactory) {
        if (sessionFactory != null) {
            HibernateUtil.closeSessionFactory();
        }
    }
}
