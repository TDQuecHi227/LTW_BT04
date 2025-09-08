package service;

import entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    void create(Category category);
    void update(Category category);
    void delete(int id);
    Category findById(int id);
    List<Category> findByUserId(int id);
}
