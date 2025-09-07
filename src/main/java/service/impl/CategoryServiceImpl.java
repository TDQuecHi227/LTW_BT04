package service.impl;

import dao.GenericDao;
import dao.impl.CategoryDaoImpl;
import entity.Category;
import service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    GenericDao<Category> categoryDao = new CategoryDaoImpl(Category.class);
    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public void create(Category category) {
        categoryDao.create(category);
    }

    @Override
    public void update(Category category) {
        categoryDao.update(category);
    }

    @Override
    public void delete(int id) {
        categoryDao.delete(id);
    }

    @Override
    public Category findById(int id) {
        return categoryDao.findById(id);
    }
}
