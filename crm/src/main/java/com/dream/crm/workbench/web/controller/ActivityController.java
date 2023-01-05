package com.dream.crm.workbench.web.controller;

import com.dream.crm.commons.contants.Contants;
import com.dream.crm.commons.pojo.ReturnObject;
import com.dream.crm.commons.utils.DateUtils;
import com.dream.crm.commons.utils.UUIDUtils;
import com.dream.crm.settings.pojo.User;
import com.dream.crm.settings.services.impl.UserServiceImpl;
import com.dream.crm.workbench.pojo.Activity;
import com.dream.crm.workbench.services.impl.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ActivityServiceImpl activityService;

    /**
     * 获取用户信息并跳转到市场活动页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //获取所有的用户
        List<User> userList = userService.selectAllUsers();
        //将用户信息发送到浏览器
        request.setAttribute("usersList",userList);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    /**
     * 向数据库中插入添加的数据
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/SaveCreateActivity.do")
    @ResponseBody
    public Object SaveCreateActivity(Activity activity, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            //向数据库中插入数据
            int saveCreateActivity = activityService.SaveCreateActivity(activity);
            if (saveCreateActivity > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    /**
     * 进行分页查询
     * @return
     */
    @RequestMapping("/workbench/activity/QueryActivityConditionForPage.do")
    @ResponseBody
    public Object QueryActivityConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        //封装参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",(pageNo - 1) * pageSize);
        map.put("pageSize",pageSize);
        //查询数据
        List<Activity> activityList = activityService.QueryActivityConditionForPage(map);
        int totalRows = activityService.QueryCountOfActivityByCondition(map);

        //根据结果生成响应信息
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }
}
