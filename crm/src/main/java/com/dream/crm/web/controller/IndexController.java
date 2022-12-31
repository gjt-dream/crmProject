package com.dream.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Struct;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response){
        String flagStr = "1";
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("loginAct")){
                    flagStr = "2";
                }
            }
        }
        Cookie flag = new Cookie("flag", flagStr);
        response.addCookie(flag);
        System.out.println("我要跳转了");
        //跳转到index页面
        return "index";
    }
}
