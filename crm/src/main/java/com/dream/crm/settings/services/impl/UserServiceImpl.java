package com.dream.crm.settings.services.impl;

import com.dream.crm.settings.mapper.UserMapper;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User selectUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = userMapper.selectAllUsers();
        return users;
    }
}
