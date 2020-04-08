package com.inspur.db.dao;


import com.inspur.db.entity.User;

public interface UserMapper {



    int insert(User record);

    String selectByPrimaryKey(String username);



}