package com.inspur.db.service;


import com.inspur.db.entity.User;

public interface UserService {
    void addUser(User user);
    String getPasswordByUsername(String username);
}
