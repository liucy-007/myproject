package com.inspur.db.service;


import com.inspur.db.dao.UserMapper;
import com.inspur.db.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

import static com.inspur.db.util.CommonUtil.getUTCDate;


@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;


    @Override
    public void addUser(User user) {
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        user.setCreateTime(getUTCDate());
        userMapper.insert(user);
    }
    @Override
    public String getPasswordByUsername(String username){
        String passWord = userMapper.selectByPrimaryKey(username);
        return passWord;
    }
}
