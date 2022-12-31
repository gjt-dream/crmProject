package com.dream.crm.settings.services;

import com.dream.crm.settings.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //查询指定用户
    User selectUserByLoginActAndPwd(Map<String,Object> map);

    //查询所有的用户
    List<User> selectAllUsers();
}
