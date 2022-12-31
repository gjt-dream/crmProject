package com.dream.crm.settings.web.controller;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.pojo.ReturnObject;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 跳转到登入页
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        System.out.println("我进来了");
        return "settings/qx/user/login";
    }

    /**
     * 登入验证
     * @param loginAct 账号
     * @param loginPwd 密码
     * @param isRemPwd 是否记住密码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response){
        //封装参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service层方法，查询用户
        User user=userService.selectUserByLoginActAndPwd(map);

        //根据查询结果，生成响应信息
        ReturnObject returnObject=new ReturnObject();
        if(user==null){
            //登录失败,用户名或者密码错误
            returnObject.setCode("0");
            returnObject.setMessage("用户名或者密码错误");
        }else {//进一步判断账号是否合法
            //user.getExpireTime()   //2019-10-20
            //        new Date()     //2020-09-10
            String nowStr = DateUtils.formateDateTime(new Date());
            if (nowStr.compareTo(user.getExpireTime()) > 0) {
                //登录失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getLockState())) {
                //登录失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //登录失败，ip受限
//                String allowIps = user.getAllowIps();
//                System.out.println("允许访问地址" + allowIps);
//                String remoteAddr = request.getRemoteAddr();
//                System.out.println("本机地址：" + remoteAddr);
//
//                boolean contains = user.getAllowIps().contains(request.getRemoteAddr());
//                System.out.println("是否允许访问：" + contains);
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            } else {
                //登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //向Session中保存 user对象
                HttpSession session = request.getSession();
                session.setAttribute(Contants.SESSION_USER,user);
                System.out.println(session.getAttribute(Contants.SESSION_USER).toString());

                //将 cookie 响应到客户端
                if ("true".equals(isRemPwd)){
                    Cookie act = new Cookie("loginAct", user.getLoginAct());
                    act.setMaxAge(60 * 60 * 24 * 10);
                    act.setPath(request.getContextPath());
                    response.addCookie(act);
                    Cookie pwd = new Cookie("loginPwd", user.getLoginPwd());
                    pwd.setMaxAge(60 * 60 * 24 * 10);
                    pwd.setPath(request.getContextPath());
                    response.addCookie(pwd);
                    Cookie flag = new Cookie("flag", "2");
                    flag.setPath(request.getContextPath());
                    response.addCookie(flag);
                }else {
                    Cookie act = new Cookie("loginAct", "");
                    act.setMaxAge(0);
                    act.setPath(request.getContextPath());
                    response.addCookie(act);
                    Cookie pwd = new Cookie("loginPwd", "");
                    pwd.setMaxAge(0);
                    pwd.setPath(request.getContextPath());
                    response.addCookie(pwd);
                    Cookie flag = new Cookie("flag", "1");
                    flag.setPath(request.getContextPath());
                    response.addCookie(flag);
                }

            }

        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookie
        Cookie act = new Cookie("loginAct", "");
        act.setMaxAge(0);
        response.addCookie(act);
        Cookie pwd = new Cookie("loginPwd", "");
        pwd.setMaxAge(0);
        response.addCookie(pwd);

        //销毁session
        session.invalidate();

        return "redirect:/";
    }
}
