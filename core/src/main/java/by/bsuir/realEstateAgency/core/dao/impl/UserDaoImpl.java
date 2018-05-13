package by.bsuir.realEstateAgency.core.dao.impl;

import by.bsuir.realEstateAgency.core.dao.UserDao;
import by.bsuir.realEstateAgency.core.model.User;
import by.bsuir.realEstateAgency.core.model.PassportData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
        if(user.getPassport() != null) {
            session.saveOrUpdate(user.getPassport());
        }
    }

    @Override
    public User get(Long key) {
        return sessionFactory.getCurrentSession().get(User.class, key);
    }

    @Override
    public List<User> findAll(int offset, int limit) {
        return sessionFactory.getCurrentSession().createQuery("Select u from User u")
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long count() {
        return (Long) sessionFactory.getCurrentSession().createQuery("select count(u) from User u").getSingleResult();
    }

    @Override
    public void remove(Long key) {
        removeList(Arrays.asList(key));
    }

    @Override
    public void removeList(List<Long> keys) {
        if(keys.size()>0) {
            Query qPassort = sessionFactory.getCurrentSession().createQuery("DELETE FROM PassportData p WHERE p.user.id IN (:list)");
            qPassort.setParameterList("list", keys);
            qPassort.executeUpdate();
            Query q = sessionFactory.getCurrentSession().createQuery("DELETE FROM User u WHERE u.id IN (:list)");
            q.setParameterList("list", keys);
            q.executeUpdate();
        }
    }
}
