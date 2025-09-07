package service;

import entity.User;

public interface UserService {
    User login (String username, String password);
}
