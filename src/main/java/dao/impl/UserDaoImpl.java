package dao.impl;

import config.JPAConfig;
import dao.UserDao;
import entity.User;
import jakarta.persistence.EntityManager;

public class UserDaoImpl extends AbstractDAOImpl<User> implements UserDao {

    public UserDaoImpl(Class<User> entityClass) {
        super(entityClass);
    }
    @Override
    public User findByUsername(String username) {
        EntityManager em =  JPAConfig.getEntityManager();
        return em.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
    }
}
