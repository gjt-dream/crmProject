package com.dream.crm.workbench.web.controller;

import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //获取所有的用户
        List<User> userList = userService.selectAllUsers();
        //将用户信息发送到浏览器
        request.setAttribute("usersList",userList);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }
}
