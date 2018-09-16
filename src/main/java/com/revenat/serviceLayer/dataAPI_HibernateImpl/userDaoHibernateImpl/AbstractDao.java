package com.revenat.serviceLayer.dataAPI_HibernateImpl.userDaoHibernateImpl;

import com.revenat.serviceLayer.dataAPI.dao.Dao;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbstractDao<T, ID extends Serializable> implements Dao<T, ID> {

    private Class<T> persistentClass;
    private Session session;

    private CriteriaBuilder builder;
    private CriteriaQuery<T> criteria;
    private Root<T> root;

    public AbstractDao(Session session) {
        Objects.requireNonNull(session, "Provided session must not be null");
        this.session = session;
        this.persistentClass = determinePersistentClass();
    }

    @SuppressWarnings("unchecked")
    private Class<T> determinePersistentClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return session;
    }

    protected CriteriaBuilder getBuilder() {
        if (this.builder == null) {
            builder = getSession().getCriteriaBuilder();
        }
        return builder;
    }

    protected CriteriaQuery<T> getCriteria() {
        if (criteria == null) {
            criteria = getBuilder().createQuery(persistentClass);
        }
        return criteria;
    }

    protected Root<T> getRoot() {
        if (root == null) {
            root = getCriteria().from(persistentClass);
        }
        return root;
    }

    protected  Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public Optional<T> findById(ID id) {
        T entity = getSession().get(this.getPersistentClass(), id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        return this.findByCriteria();
    }

    protected List<T> findByCriteria(Predicate... predicates) {
        criteria = getCriteria();
        root = getRoot();

        criteria.select(root).where(predicates);

        return getSession().createQuery(criteria).getResultList();
    }

    @Override
    public void save(T entity) {
        this.getSession().saveOrUpdate(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T update(T entity) {
        return (T) getSession().merge(entity);
    }

    @Override
    public void delete(T entity) {
        this.getSession().remove(entity);
    }
}
