package dao;

import entity.User;

public interface UserDao extends GenericDao<User>{
    User findByUsername(String username);
}
