package dao.impl;

import config.JPAConfig;
import dao.CategoryDao;
import entity.Category;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoryDaoImpl extends AbstractDAOImpl<Category> implements CategoryDao {
    public CategoryDaoImpl(Class<Category> entityClass) {
        super(entityClass);
    }
    @Override
    public List<Category> findByUserId(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        return em.createQuery("select c from Category c where c.user.userId = :id", Category.class).setParameter("id", id).getResultList();
    }
}
