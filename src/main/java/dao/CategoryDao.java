package dao;

import entity.Category;

import java.util.List;

public interface CategoryDao extends GenericDao<Category>{
    List<Category> findByUserId(int id);
}
