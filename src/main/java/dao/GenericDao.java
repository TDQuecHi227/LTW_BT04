package dao;

import entity.Category;

import java.util.List;

public interface GenericDao<T> {
    List<T> findAll();
    T findById(int id);
    void create(T entity);
    void update(T entity);
    void delete(int id);
}
