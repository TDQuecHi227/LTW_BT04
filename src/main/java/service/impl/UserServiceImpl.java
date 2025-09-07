package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import entity.User;
import service.UserService;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl(User.class);
    @Override
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if(user!=null){
            return user;
        }
        return null;
    }
}
