package com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoHibernateImpl;

import com.revenat.serviceLayer.entities.User;
import com.revenat.serviceLayer.dataAPI.userDao.UserDao;
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class UserHibernateDao extends AbstractDao<User, Long> implements UserDao {

    public UserHibernateDao(Session session) {
        super(session);
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        Predicate predicate = getBuilder().equal(getRoot().get("firstName"), firstName);
        return this.findByCriteria(predicate);
    }
}
