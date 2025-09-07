package dao.impl;

import config.JPAConfig;
import dao.GenericDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public abstract class AbstractDAOImpl<T> implements GenericDao<T> {
    private Class<T> entityClass;
    public AbstractDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    @Override
    public T findById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        return em.find(entityClass, id);
    }
    @Override
    public List<T> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        return em.createQuery("select c from " +  entityClass.getSimpleName() + " c").getResultList();
    }
    @Override
    public void create(T entity) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }finally{
            em.close();
        }
    }
    @Override
    public void update(T entity) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            em.merge(entity);
            transaction.commit();
        }catch(Exception e){
            transaction.rollback();
        }finally{
            em.close();
        }
    }
    @Override
    public void delete(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            T entity = em.find(entityClass, id);
            if(entity != null){
                em.remove(entity);
                transaction.commit();
            }
        }catch(Exception e){
            transaction.rollback();
        }finally {
            em.close();
        }
    }
}
